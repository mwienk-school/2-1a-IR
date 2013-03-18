package nl.utwente.ir.controllers;

import java.util.Locale;

import nl.utwente.ir.services.ISearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private ISearchService searchService;
	
	@Autowired
	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}
	
	/**
	 * Simply selects the home view
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		if(!searchService.isActive()) {
			model.addAttribute("error", "The index hasn't been built, please build an index first");
		}
		model.addAttribute("pageClass", "home");
		return "facetedSearch";
	}	
	
}