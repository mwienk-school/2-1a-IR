package nl.utwente.ir;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.utwente.ir.analysis.CustomAnalyzer;
import nl.utwente.ir.parser.Topic;
import nl.utwente.ir.parser.TopicParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.TermQuery;

import nl.utwente.ir.helper.TermFreq;
import nl.utwente.ir.helper.TermFreqComparator;

/** 
 * TopicSearch parses a topics file and returns the a trec_eval formatted run report
 */
public class TopicSearch {
    protected static TermFreqComparator comparator = new TermFreqComparator();
	private TopicSearch() {
	}
	
	/**
     * Returns the top terms for a list of TermFreqVectors.
     *
     * @param tfvlist a list of termvectors
     * @param numTermsToReturn number of terms to return
     * @return A Collection of TermFreqs
     */
    public static List<TermFreq> getTopTerms(List<TermFreqVector> tfvlist, int numTermsToReturn)
    {
    	List<TermFreq> result = new ArrayList<TermFreq>();
    	for(TermFreqVector tfv : tfvlist) {
    		String[] terms = tfv.getTerms();
    		int [] freqs = tfv.getTermFrequencies();
    		for (int i = 0; i < terms.length; i++)
    		{
    			result.add(new TermFreq(terms[i], freqs[i]));
    		}
    	}
    	Collections.sort(result, comparator);
    	if (numTermsToReturn < result.size())
   		{
   			result = result.subList(0, numTermsToReturn);
   		}
   		return result;
    }

	/** 
	 * Main entry 
	 */
	public static void main(String[] args) throws Exception {
		// Fields to query
		String[] fields = { "ArticleTitle", "AbstractText" };

		// Topics to query
		List<Topic> topics = null;

		// Check if a filename is supplied
		if (args.length != 1) throw new Exception("You have to use this program with 1 argument (a topics file).");

		// Create the indexreader etc.
		IndexReader reader = IndexReader.open(FSDirectory
				.open(new File("index")));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new CustomAnalyzer(Version.LUCENE_31);

		// Parse the topics file
		TopicParser tparser = new TopicParser();
		tparser.parseFile(args[0]);
		topics = tparser.getTopics();

		// Create a parser for query (multiple fields)
		MultiFieldQueryParser qparser = new MultiFieldQueryParser(Version.LUCENE_31, fields, analyzer);

		// Perform a search for each topic
		for (Topic topic : topics) {
			Query query = qparser.parse(topic.getTitle());
			doSearch(topic.getId(), query, searcher);
		}
		
		// Close the program
		searcher.close();
		reader.close();
	}

	/**
	 * Performs a search and prints the trec_eval format run results
	 * 
	 * @param topicID
	 * @param query
	 * @param searcher
	 * @throws Exception
	 */
	public static void doSearch(int topicID, Query query, IndexSearcher searcher) throws Exception {
		// Perform a first search on the topic
		TopDocs results = searcher.search(query, 1000);
		ScoreDoc[] hits = results.scoreDocs;
		
		// Relevance feedback
		List<TermFreqVector> termVectors = new ArrayList<TermFreqVector>();

		// Collect the term vectors for the first 50 hits 
		for(int i = 0; i < 50 && i < hits.length -1; i++) {
			TermFreqVector titleTV = searcher.getIndexReader().getTermFreqVector(hits[i].doc, "ArticleTitle");
			TermFreqVector abstractTV = searcher.getIndexReader().getTermFreqVector(hits[i].doc, "AbstractText");
			if(titleTV != null) termVectors.add(titleTV);
			if(abstractTV != null) termVectors.add(abstractTV);
		}
		
		// Receive the top terms
		List<TermFreq> terms = getTopTerms(termVectors, 10);
		
		// Perform the relevance feedback query
        BooleanQuery relFeedQuery = new BooleanQuery();
        query.setBoost(35);
        relFeedQuery.add(query, BooleanClause.Occur.MUST);
        
        // Add in the relevant terms
        for (TermFreq term : terms) {
        	TermQuery termQuery;
        	
        	// ArticleTitle
            termQuery = new TermQuery(new Term("ArticleTitle", term.getTerm()));
            termQuery.setBoost(term.getFreq()); // Set frequency as the boost level
            BooleanClause clause = new BooleanClause(termQuery, BooleanClause.Occur.SHOULD);
            relFeedQuery.add(clause);

            // AbstractText
            termQuery = new TermQuery(new Term("AbstractText", term.getTerm()));
            termQuery.setBoost(term.getFreq());
            clause = new BooleanClause(termQuery, BooleanClause.Occur.SHOULD);
            relFeedQuery.add(clause);
        }

        // Perform the new search query (with relevance feedback)
        results = searcher.search(relFeedQuery, 1000);
        hits = results.scoreDocs;

		int rank = 1;
		for (ScoreDoc hit : hits) {
			Document doc = searcher.doc(hit.doc);
		
			// Print in trec_eval format
			System.out.println(topicID + "\t" 
								+ "Q0" + "\t" 
								+ doc.get("PMID") + "\t" 
								+ rank++ + "\t" 
								+ hit.score + "\t" 
								+ "s1238531");
		}
	}
}
