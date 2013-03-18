package nl.utwente.ir.services.search;

import javax.xml.stream.events.XMLEvent;

import org.apache.lucene.document.Field;
import org.springframework.stereotype.Service;

import nl.utwente.ir.abstracts.AXMLParser;


/**
 * Parses Verhalenbank XML files to objects
 */
@Service
public class VerhalenbankParser extends AXMLParser {

	@Override
	protected void parse() throws Exception {
		int eventType = xmlr.getEventType();
		String curElement = "";
		VerhalenbankIndexer indexer = new VerhalenbankIndexer();
		Verhaal current = null;
		while (xmlr.hasNext()) {
			eventType = xmlr.next();
			switch (eventType) {
			case XMLEvent.START_ELEMENT:
				// Use curElement as simplified XPath expression
				curElement = curElement + "/" + xmlr.getName().toString();
				if (curElement.equals(Verhaal.XPATH_START)) {
					if (current != null) {
						// A new citation is being parsed, save the former
						indexer.indexDocument(current);
					}
					current = new Verhaal();
				}
				break;
			case XMLEvent.CHARACTERS:
				// Data field, should be used in an object
				if (Verhaal.XPATH_FIELDS.containsKey(curElement)) {
					String text = xmlr.getText();
					XMLField field = Verhaal.XPATH_FIELDS.get(curElement);
					if(field.getIndex() == Field.Index.NOT_ANALYZED) {
						text = text.toLowerCase();
					}
					if(field.hasMultipleFields()) {
						String[] values = text.split(field.getMultipleFieldSeparator());
						for(String value : values) {
							current.setMultipleValuedFieldValue(field.getFieldname(), value);
						}
					} else {
						current.setValue(field.getFieldname(), text);
					}
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
