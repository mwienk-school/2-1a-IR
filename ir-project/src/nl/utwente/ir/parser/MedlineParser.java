package nl.utwente.ir.parser;

import javax.xml.stream.events.XMLEvent;

/**
 * Parses Medline XML files to objects
 */
public class MedlineParser extends AXMLParser {

	/**
	 * Main for the parser
	 */
	public static void main(String[] args) throws Exception {
		MedlineParser xfp = new MedlineParser();
		for (String arg : args) {
			xfp.parseFile(arg);
		}
	}

	@Override
	protected void parse() throws Exception {
		int eventType = xmlr.getEventType();
		String curElement = "";
		MedlineIndexer indexer = new MedlineIndexer();
		MedlineCitation current = null;
		while (xmlr.hasNext()) {
			eventType = xmlr.next();
			switch (eventType) {
			case XMLEvent.START_ELEMENT:
				// Use curElement as simplified XPath expression
				curElement = curElement + "/" + xmlr.getName().toString();
				if (curElement.equals(MedlineCitation.XPATH_START)) {
					if (current != null) {
						// A new citation is being parsed, save the former
						indexer.indexDocument(current);
					}
					current = new MedlineCitation();
				}
				break;
			case XMLEvent.CHARACTERS:
				// Data field, should be used in an object
				if (MedlineCitation.XPATH_FIELDS.containsKey(curElement)) {
					// Field needs to be indexed
					current.setValue(MedlineCitation.XPATH_FIELDS.get(curElement).getFieldname(), xmlr.getText());
				}
				break;
			case XMLEvent.END_ELEMENT:
				// Use curElement as simplified XPath expression
				curElement = curElement.substring(0,curElement.lastIndexOf("/"));
				break;
			}
		}
		if (current != null) indexer.indexDocument(current); // Save the last citation
		
		// Close
		indexer.endIndexing();
	}
}
