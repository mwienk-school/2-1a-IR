package nl.utwente.ir.controllers;

import java.util.List;

import nl.utwente.ir.services.ISearchService;
import nl.utwente.ir.services.SearchService;
import nl.utwente.ir.services.search.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the search page.
 */
@Controller
@RequestMapping("/search")
public class FacetedSearchController {
	
	private ISearchService searchService;
	
	@Autowired
	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String process(Model model) {
		if(!searchService.isActive()) {
			model.addAttribute("error", "The index hasn't been built, please build an index first");
		}
		model.addAttribute("pageClass","home");
		return "facetedSearch";
	}
	
	@RequestMapping(method = RequestMethod.POST) 
	public String processUrl(@RequestParam("query") String query, @RequestParam("extreem") String extreem) {
		extreem = extreem.equals("on") ? "?extreme=on" : "extreem:nee?extreme=off";
		return "redirect:/search/" + query + "/" + extreem;
	}
	
	@RequestMapping(value="/{query}/",method = RequestMethod.GET)
	public String processQuery(@PathVariable String query, @RequestParam("extreme") String extreem, Model model) {
		return processQueryWithFilters(query, "", extreem, model);
	}
		
	@RequestMapping(value="/{query}/{filters}",method = RequestMethod.GET)
	public String processQueryWithFilters(@PathVariable String query, @PathVariable String filters, @RequestParam("extreme") String extreem, Model model) {
		if(!searchService.isActive()) {
			model.addAttribute("error", "The index hasn't been built, please build an index first");
			return "facetedSearch";
		}
		if(!extreem.equals("on")) {
			// Extreme filter: ignore all extreme facet filters, add only nonextreme
			filters = filters.replace("extreem:ja,nee", "");
			filters = filters.replace("extreem:nee,ja", "");
			filters = filters.replace("extreem:ja", "");
			filters = filters.replace("extreem:nee", "");
			filters += "$extreem:nee";
		}
		model.addAttribute("title", query);
		model.addAttribute("query", query);
		model.addAttribute("filters", SearchService.handleFilters(filters));
		try {
			List<SearchResult> results = searchService.search(query, filters);
			List<SearchResult> queryResults = searchService.search(query, "");
			model.addAttribute("results", results);
			model.addAttribute("count", results.size());
			model.addAttribute("facets", searchService.getFacets(queryResults, results));
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "searchResults";
	}
}
