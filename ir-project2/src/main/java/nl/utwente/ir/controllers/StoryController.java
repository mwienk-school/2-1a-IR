package nl.utwente.ir.controllers;

import nl.utwente.ir.services.ISearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the search page.
 */
@Controller
@RequestMapping("/story")
public class StoryController {
	
	private ISearchService searchService;
	
	@Autowired
	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String onPost(@PathVariable("id") String id, Model model) {
		try {
			model.addAttribute("story", searchService.get(id));
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "story";
	}
}
