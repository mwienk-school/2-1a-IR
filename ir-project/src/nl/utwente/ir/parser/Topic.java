package nl.utwente.ir.parser;

/**
 * A topic in a trec topics file
 * @author mark
 *
 */
public class Topic {
	// XML Field information (in simplified Xpath)
	public static final String XPATH_START = "/TOPICS/TOPIC";
	public static final String XPATH_ID    = XPATH_START + "/ID";
	public static final String XPATH_TITLE = XPATH_START + "/TITLE";
	
	private String title;
	private int id;
	
	/**
	 * Returns the Title
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title field (search query)
	 * @param title
	 */
	public void setTitle(String title) {
		// Remove unnecessary text
		title = title.replace("Provide information about ", "");
		title = title.replace("Describe the procedure or methods for ", "");
		title = title.replace("Provide information on ", "");
		this.title = title;
	}
	
	/**
	 * Return the ID
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/** 
	 * Set the ID
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
}
