package nl.utwente.ir.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.utwente.ir.services.ISearchService;
import nl.utwente.ir.services.SearchService;
import nl.utwente.ir.services.search.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the search page.
 */
@Controller
@RequestMapping("/advanced")
public class AdvancedSearchController {
	
	private ISearchService searchService;
	
	@Autowired
	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String processAdvancedSearch(Model model) {
		if(!searchService.isActive()) {
			model.addAttribute("error", "The index hasn't been built, please build an index first");
		}
		model.addAttribute("pageClass", "advanced");
		return "advancedSearch";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String processQueryWithFilters(HttpServletRequest request, Model model) {
		String filters = "";
		filters += "idnummer:" 		+ request.getParameter("idnummer") + "$";
		filters += "type:" 			+ request.getParameter("type") + "$";
		//filters += "notulist:" 		+ request.getParameter("notulist") + "$";
		filters += "taal:" 			+ request.getParameter("taal") + "$";
		//filters += "schriftbron:" 	+ request.getParameter("schriftbron") + "$";
		filters += "regio:" 		+ request.getParameter("regio") + "$";
		//filters += "kloekenummer:" 	+ request.getParameter("kloekenummer") + "$";
		//filters += "verteller:" 	+ request.getParameter("verteller") + "$";
		filters += "datering:" 		+ request.getParameter("datering") + "$";
		filters += "literair:" 		+ request.getParameter("literair") + "$";
		filters += "subgenre:" 		+ request.getParameter("subgenre") + "$";
		//filters += "motieven:" 		+ request.getParameter("motieven") + "$";
		//filters += "samenvatting:" 	+ request.getParameter("samenvatting") + "$";
		filters += "trefwoorden:" 	+ request.getParameter("trefwoorden") + "$";
		//filters += "namen:" 		+ request.getParameter("namen") + "$";
		//filters += "opmerkingen:" 	+ request.getParameter("opmerkingen") + "$";
		//filters += "corpus:" 		+ request.getParameter("corpus") + "$";
		//filters += "aardbron:" 		+ request.getParameter("aardbron") + "$";
		
		if(request.getParameter("extreme").equals("off")) {
			filters += "extreem:nee";
		}
		
		model.addAttribute("filters", SearchService.handleFilters(filters));
		try {
			List<SearchResult> results;
			if(request.getParameter("query") != null && !request.getParameter("query").equalsIgnoreCase("")) {
				results = searchService.search(request.getParameter("query"), filters);
				model.addAttribute("title", request.getParameter("query"));
				model.addAttribute("query", request.getParameter("query"));
			} else {
				results = searchService.search(filters);
				model.addAttribute("title", "Advanced search");
			}
			model.addAttribute("results", results);
			model.addAttribute("count", results.size());
			model.addAttribute("pageClass", "advancedsearch-results");
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "searchResults";
	}
}
