package nl.utwente.ir.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * A citation used in the medline data
 * @author mark
 */
public class MedlineCitation {
	// Start XPATH expression for the citations
	public static final String XPATH_START = "/MedlineCitationSet/MedlineCitation";
	// Fields to index (by XPATH expression)
	public static final Map<String, XMLField> XPATH_FIELDS = new HashMap<String, XMLField>();
	static {
		XPATH_FIELDS.put(XPATH_START + "/PMID", new XMLField("PMID", "numeric"));
		XPATH_FIELDS.put(XPATH_START + "/Article/ArticleTitle", new XMLField("ArticleTitle", "text"));
		XPATH_FIELDS.put(XPATH_START + "/Article/Abstract/AbstractText", new XMLField("AbstractText", "text"));
	}
	// Field to indicate the ID
	public static final String IDFIELD = "PMID";

	// Stores values of a Citation
	private Map<String, String> map;

	/**
	 * Set the value of a field in the citation
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void setValue(String key, String value) throws Exception {
		if (map.containsKey(key)) {
			map.put(key, value);
		} else {
			throw new Exception("The used key is not a field specified for indexing.");
		}
	}

	/**
	 * Return a specified value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return map.get(key);
	}

	/**
	 * A citation used in the medline data
	 */
	public MedlineCitation() {
		map = new HashMap<String, String>();
		for (XMLField key : XPATH_FIELDS.values()) {
			// Store field information in the map
			map.put(key.getFieldname(), "");
		}
	}
	
	/**
	 * Print the citation's ID
	 */
	public String toString() {
		return this.map.get(IDFIELD);
	}
}
