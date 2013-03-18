package nl.utwente.ir.services.search;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Field;

/**
 * A document in the verhalenbank
 * @author mark
 */
public class Verhaal {
	// Start XPATH expression for the documents
	public static final String XPATH_START = "/verhalenbank/document";
	// Fields to index (by XPATH expression)
	public static final Map<String, XMLField> XPATH_FIELDS = new HashMap<String, XMLField>();
	static {
		XPATH_FIELDS.put(XPATH_START + "/idnummer", new XMLField("idnummer", "text"));
		XPATH_FIELDS.put(XPATH_START + "/titel", new XMLField("titel", "text"));
		XPATH_FIELDS.put(XPATH_START + "/tekst", new XMLField("tekst", "text"));
		XPATH_FIELDS.put(XPATH_START + "/verhaalopbouw", new XMLField("verhaalopbouw", "text"));
		XPATH_FIELDS.put(XPATH_START + "/regio", new XMLField("regio", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/volksverhaal_type", new XMLField("type", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/atu_omschrijving", new XMLField("atu_omschrijving", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/taal", new XMLField("taal", "text", Field.Index.NOT_ANALYZED, " & "));
		XPATH_FIELDS.put(XPATH_START + "/literair", new XMLField("literair", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/extreem", new XMLField("extreem", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/subgenre", new XMLField("subgenre", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/datering", new XMLField("datering", "text", Field.Index.NOT_ANALYZED));
		XPATH_FIELDS.put(XPATH_START + "/trefwoorden", new XMLField("trefwoorden", "text", Field.Index.NOT_ANALYZED, ","));
	}
		
	// Field to indicate the ID
	public static final String IDFIELD = "idnummer";

	// Stores values of a Citation
	private Map<String, String> map;
	private Map<String, List<String>> multipleValuedMap;

	/**
	 * Set the value of a field in the document
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void setValue(String key, String value) throws Exception {
		switch (key) {
		case "regio" :
			value = value.replaceAll("\\(.*\\)", "").trim(); // Remove district from result
			break;
		case "datering" :
			value = value.replaceAll(".*(\\d{4}).*", "$1").trim(); // Only store year
			break;
		}
		if (map.containsKey(key)) {
			String old = map.get(key);
			map.put(key, old + value);
		} else {
			throw new Exception("The used key is not a field specified for indexing.");
		}
	}
	
	/**
	 * Set the value of a multipleValuedField
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void setMultipleValuedFieldValue(String key, String value) throws Exception {
		if(multipleValuedMap.containsKey(key)) {
			multipleValuedMap.get(key).add(value);
		}
	}
	
	/**
	 * Returns a list of items in a multiplevaluedmap
	 * @param key
	 */
	public List<String> getMultipleValuedField(String key) {
		return multipleValuedMap.get(key);
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
	 * A document used in the data
	 */
	public Verhaal() {
		map = new HashMap<String, String>();
		multipleValuedMap = new HashMap<String, List<String>>();
		for (XMLField key : XPATH_FIELDS.values()) {
			// Store field information in the map
			if (key.hasMultipleFields()) {
				multipleValuedMap.put(key.getFieldname(), new ArrayList<String>());
			} else {
				map.put(key.getFieldname(), "");
			}
		}
	}
	
	/**
	 * Print the ID
	 */
	public String toString() {
		return this.map.get(IDFIELD).toLowerCase();
	}
}
