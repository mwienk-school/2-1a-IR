package nl.utwente.ir.abstracts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLInputFactory;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * Abstract XML Parser class
 * 
 * Uses Streaming XML (stax2) parser
 * @author mark
 *
 */
public abstract class AXMLParser {
	protected XMLStreamReader2 xmlr = null;
	protected boolean printLog = false;
	
	/**
	 * Set the XML reader
	 * @param filename
	 * @throws Exception
	 */
	public void setReader(String filename) throws Exception {
		XMLInputFactory2 xmlif = null;
		try {
			xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
			xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
			xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
			xmlif.configureForSpeed();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(printLog) System.out.println("Starting to parse " + filename);

		// Check whether the file is GZipped
		InputStream stream = new FileInputStream(filename);
		if (filename.endsWith(".gz")) {
			stream = new GZIPInputStream(stream);
		}
		xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(filename, stream);
	}
	
	/**
	 * Insert parse logic
	 */
	protected abstract void parse() throws Exception;
	
	/**
	 * Parse the XML File
	 * @param filename
	 * @throws Exception 
	 */
	public void parseFile(String filename) throws Exception {
		long starttime = System.currentTimeMillis();
		this.setReader(filename);
		this.parse();
		if(printLog)	System.out.println(" completed in "
				+ (System.currentTimeMillis() - starttime) + " ms");
	}
	
	public AXMLParser() {}
	
	/**
	 * Print parser debug output
	 * @param printLog
	 */
	public AXMLParser(boolean printLog) {
		this.printLog = printLog;
	}
}
