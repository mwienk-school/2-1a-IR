package nl.utwente.ir.parser;

/**
 * Stores information about an XML Field (for a mapping to an index field).
 * @author mark
 *
 */
public class XMLField {
	private String fieldname; 	// The name of the field
	private String type;		// The type of the field (lucene index type)
	
	/**
	 * Return the fieldname
	 * @return
	 */
	public String getFieldname() {
		return fieldname;
	}
	
	/**
	 * Set the fieldname
	 * @param fieldname
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	
	/**
	 * Return the type
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set the type
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Stores information about an XML Field (for a mapping to an index field).
	 * @param fieldname
	 * @param type
	 */
	public XMLField(String fieldname, String type) {
		this.fieldname = fieldname;
		this.type = type;
	}

}
