package nl.utwente.ir.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.utwente.ir.abstracts.AXMLParser;

@Service
public class IndexService implements IIndexService {

	private AXMLParser parser;
	
	private static final Logger logger = LoggerFactory.getLogger(IndexService.class);
	
	@Autowired
	public void setXMLParser(AXMLParser parser) {
		this.parser = parser;
	}
	
	public void index() {
		try {
			parser.parseFile("/home/mark/Downloads/verhalenbank.xml.gz");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
