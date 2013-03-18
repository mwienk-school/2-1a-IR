package nl.utwente.ir.controllers;

import nl.utwente.ir.services.IIndexService;
import nl.utwente.ir.services.ISearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/indexer")
public class IndexController {
	
	private IIndexService indexService; // The index service to be used
	private ISearchService searchService;
	
	@Autowired
	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}
	
	@Autowired
	public void setIndexService(IIndexService indexService) {
		this.indexService = indexService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model) {
		indexService.index();
		searchService.init();
		model.addAttribute("title", "Done!");
		model.addAttribute("body", "The files are indexed");
		return "page";
	}
}
