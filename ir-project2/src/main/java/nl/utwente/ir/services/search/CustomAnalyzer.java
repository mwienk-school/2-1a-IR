package nl.utwente.ir.services.search;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

/**
 * CustomAnalyzer for Lucene queries. Uses multiple token filters.
 */
public class CustomAnalyzer extends StopwordAnalyzerBase {
	// localStopwords override the StopwordAnalyzerBase stopwords
	private CharArraySet localStopwords; 

	/**
	 * CustomAnalyzer for Lucene queries. Uses multiple token filters.
	 * @param version
	 */
	public CustomAnalyzer(Version version) {
		super(version);
		try {
			URL url = getClass().getResource("/stopwords.txt");
			localStopwords = loadStopwordSet(new File(url.getPath()), matchVersion);
		} catch (IOException e) {
			localStopwords = stopwords;
		}
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		// Create tokenizer
	    final StandardTokenizer src = new StandardTokenizer(matchVersion, reader);
	    src.setMaxTokenLength(255);
	    
	    // Add token filters
	    TokenStream tok = new StandardFilter(matchVersion, src);
	    tok = new LowerCaseFilter(matchVersion, tok);
	    tok = new StopFilter(matchVersion, tok, localStopwords);
	    tok = new PorterStemFilter(tok);
	    return new TokenStreamComponents(src, tok) {
	    	@Override
	      	protected boolean reset(final Reader reader) throws IOException {
	    		src.setMaxTokenLength(255);
	        	return super.reset(reader);
	    	}
	    };
	}
}
