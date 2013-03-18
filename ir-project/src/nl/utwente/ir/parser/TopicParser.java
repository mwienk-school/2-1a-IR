package nl.utwente.ir.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.XMLEvent;

/**
 * Parses a TREC topics file to Topic objects
 * @author mark
 *
 */
public class TopicParser extends AXMLParser {
	private List<Topic> topics;

	@Override
	protected void parse() throws Exception {
		int eventType = xmlr.getEventType();
		String curElement = "";
		Topic current = null;
		while (xmlr.hasNext()) {
			eventType = xmlr.next();
			switch (eventType) {
			case XMLEvent.START_ELEMENT:
				// Use curElement as simplified XPath expression
				curElement = curElement + "/" + xmlr.getName().toString();
				if(curElement.equals(Topic.XPATH_START)) {
					if (current != null) {
						// A new topic is found, store the previous one
						topics.add(current);
					}
					current = new Topic();
				}
				break;
			case XMLEvent.CHARACTERS:
				// Data fields for topics
				
				// Title
				if (curElement.equals(Topic.XPATH_TITLE)) {
					current.setTitle(xmlr.getText());
				} 
				// Topic ID
				else if (curElement.equals(Topic.XPATH_ID)) {
					current.setId(Integer.parseInt(xmlr.getText()));
				}
				
				break;
			case XMLEvent.END_ELEMENT:
				// Use curElement as simplified XPath expression
				curElement = curElement.substring(0, curElement.lastIndexOf("/"));
				break;
			}
		}
		// Add the last topic
		topics.add(current);
	}

	/**
	 * Return the topics
	 * @return The topics in a List
	 */
	public List<Topic> getTopics() {
		return this.topics;
	}

	/**
	 * A parser can parse topics from a TREC topics file
	 */
	public TopicParser() {
		this.topics = new ArrayList<Topic>();
	}

}
