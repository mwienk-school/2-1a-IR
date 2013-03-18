package nl.utwente.ir.services;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import nl.utwente.ir.services.search.Facet;
import nl.utwente.ir.services.search.SearchResult;

public interface ISearchService {
	
	/**
	 * Search with just filters
	 * @param filters
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public List<SearchResult> search(String filters) throws ParseException,	IOException;
	
	/**
	 * Search with a query and filter
	 * @param query
	 * @param filters
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public List<SearchResult> search(String query, String filters) throws ParseException, IOException;
	
	/**
	 * Get a single searchresult
	 * @param id, the unique id of a document
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public SearchResult get(String id) throws ParseException, IOException;
	
	/**
	 * Return all facets that are applicable to the search results set
	 * @param queryResults
	 * @param results
	 * @return
	 */
	public Collection<Facet> getFacets(List<SearchResult> queryResults, List<SearchResult> results);
	
	/**
	 * Return the search service status
	 * @return
	 */
	public boolean isActive();
	
	/**
	 * Initializes the search service, returns the status 
	 */
	public boolean init();


}
