package nl.utwente.ir.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.utwente.ir.services.search.CustomAnalyzer;
import nl.utwente.ir.services.search.Facet;
import nl.utwente.ir.services.search.SearchResult;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements ISearchService {
	
	public static final String[] fields = { "titel", "tekst" };
	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	
	private IndexReader reader;
	private IndexSearcher searcher;
	private Analyzer analyzer;
	private MultiFieldQueryParser parser;
	private QueryParser idqueryparser;
	private boolean active;
	
	public SearchService() {
		init();
	}
	
	@Override
	public boolean init() {
		try {
			reader = IndexReader.open(FSDirectory.open(new File("/home/mark/Downloads/index")));
			searcher = new IndexSearcher(reader);
			analyzer = new CustomAnalyzer(Version.LUCENE_31);
			parser = new MultiFieldQueryParser(Version.LUCENE_31, fields, analyzer);
			idqueryparser = new QueryParser(Version.LUCENE_31, "idnummer", analyzer);
			active = true;
		} catch (Exception e) {
			active = false;
		}
		return active;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public List<SearchResult> search(String filters) throws ParseException, IOException {
		BooleanQuery searchQuery = new BooleanQuery();

		Map<String,String[]> filterMap = parseFilters(filters);
		// Add the filter options as an OR query
		for (Map.Entry<String, String[]> entry : filterMap.entrySet()) {
			BooleanQuery filterQuery = new BooleanQuery();
			filterQuery.setMinimumNumberShouldMatch(1);
			for(String value : entry.getValue()) {
				filterQuery.add(new TermQuery(new Term(entry.getKey(), value.toLowerCase())), BooleanClause.Occur.SHOULD);
			}
			searchQuery.add(filterQuery, BooleanClause.Occur.MUST);
		}
		System.err.println(searchQuery.toString());
		return getSearchResults(searchQuery, 1000);
	}
	
	@Override
	public List<SearchResult> search(String strQuery, String filters) throws ParseException, IOException {
		Query query = parser.parse(strQuery);
		
		BooleanQuery searchQuery = new BooleanQuery();
		query.setBoost(35);
		searchQuery.add(query, BooleanClause.Occur.MUST);
		
		Map<String,String[]> filterMap = parseFilters(filters);

		// Add the filter options as an OR query
		for (Map.Entry<String, String[]> entry : filterMap.entrySet()) {
			BooleanQuery filterQuery = new BooleanQuery();
			filterQuery.setMinimumNumberShouldMatch(1);
			for(String value : entry.getValue()) {
				filterQuery.add(new TermQuery(new Term(entry.getKey(), value)), BooleanClause.Occur.SHOULD);
			}
			searchQuery.add(filterQuery, BooleanClause.Occur.MUST);
		}
		
		return getSearchResults(searchQuery,1000);				
	}

	@Override
	public SearchResult get(String id) throws ParseException, IOException {
		Query query = idqueryparser.parse(id);
		return getSearchResults(query, 1).get(0);
	}
	
	private List<SearchResult> getSearchResults(Query query, int limit) throws IOException {
		List<SearchResult> result = new ArrayList<SearchResult>();
		// Perform a search on query
		logger.info("Search for query: " + query);
		TopDocs results = searcher.search(query, limit);
		ScoreDoc[] hits = results.scoreDocs;
				
		for (ScoreDoc hit : hits) {
			Document doc = searcher.doc(hit.doc);
				
			SearchResult sr = new SearchResult(doc.get("idnummer"));
			sr.setTitel(doc.get("titel"));
			sr.setTekst(doc.get("verhaalopbouw"));
			sr.setExtreem(doc.get("extreem"));
			
			sr.setTaal(doc.get("taal"));
			sr.setLiterair(doc.get("literair"));
			sr.setExtreem(doc.get("extreem"));
			sr.setSubgenre(doc.get("subgenre"));
			sr.setRegio(doc.get("regio"));
			sr.setDatering(doc.get("datering"));
			sr.setType(doc.get("type"));
			sr.setATUOmschrijving(doc.get("atu_omschrijving"));
			List<String> trefwoorden = new ArrayList<String>();
			for(Fieldable field : doc.getFields()) {
				if(field.name().equals("trefwoorden")){
					trefwoorden.add(field.stringValue());
				}
			}
			sr.setTrefwoorden(trefwoorden);
			result.add(sr);
		}
		return result;
	}
	
	@Override
	public Collection<Facet> getFacets(List<SearchResult> queryResults, List<SearchResult> input) {
		Map<String, Facet> results = new HashMap<String, Facet>();
		for(SearchResult result : queryResults) {
			Map<String, String[]> facets = result.getFacets();
			for(Map.Entry<String, String[]> entry : facets.entrySet()) {
				Facet facet = results.get(entry.getKey());
				if(facet == null) {
					facet = new Facet(entry.getKey());
					results.put(entry.getKey(), facet);
				}
				for(String value : entry.getValue()) {
					facet.add(value);
				}
			}
		}
		for(SearchResult result : input) {
			// Process all properties from the searchresult 
			Map<String, String[]> facets = result.getFacets();
			for(Map.Entry<String, String[]> entry : facets.entrySet()) {
				Facet facet = results.get(entry.getKey());
				if (facet == null) {
					// Create new facet (with the key as title)
					facet = new Facet(entry.getKey());
					results.put(entry.getKey(), facet);
				}
				// Up the facet count
				for(String value : entry.getValue()) {
					facet.indent(value);
				}
			}
		}
		return results.values();
	}
	
	
	
	/**
	 * Parses the filters and returns a Map with the selected filters and options
	 * @param filters
	 * @return
	 */
	public static Map<String,String[]> parseFilters(String filters) {
		Map<String,String[]> result = new HashMap<String,String[]>(); 
		for(String filter : filters.split("\\$")) {
			if(!filter.equals("")) {
				// Only add if a filter and an option is specified
				if(!filter.substring(filter.indexOf(":") + 1).equals("")) {
					result.put(
							filter.substring(0, filter.indexOf(":")), // Title
							filter.substring(filter.indexOf(":") + 1).split(",") // Options
					);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns a HTML list of the filters
	 * @param filters
	 * @return
	 */
	public static List<String> handleFilters(String filters) {
		List<String> result = new ArrayList<String>();
		Map<String,String[]> filterMap = parseFilters(filters);
		for(Map.Entry<String, String[]> entry : filterMap.entrySet()) {
			String filter = "<div id=\"active-filter-" + entry.getKey() + "\"><span class=\"title\">" + entry.getKey() + ": </span><ul class=\"inline\">";
			for(String option : entry.getValue()) {
				filter += "<li><a id=\"" + option + "\" data-filter=\"" + entry.getKey() + "\">" + option + "</a></li>";
			}
			filter += "</ul></div>";
			result.add(filter);
		}
		return result;
	}
}
