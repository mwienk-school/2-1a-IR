package nl.utwente.ir.services.search;

import org.apache.lucene.document.Field;

/**
 * Stores information about an XML Field (for a mapping to an index field).
 * @author mark
 *
 */
public class XMLField {
	private String fieldname; 	// The name of the field
	private String type;		// The type of the field (lucene index type)
	private Field.Index index;  // Should the field be analyzed
	private String multipleFieldSeparator; // Separates multiple fields
	
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
	 * Sets the indexing type
	 * @param index
	 */
	public void setIndex(Field.Index index) {
		this.index = index;
	}
	
	/**
	 * Return the index
	 * @return
	 */
	public Field.Index getIndex() {
		return this.index;
	}
	
	public boolean hasMultipleFields() {
		return (this.multipleFieldSeparator != null);
	}
	
	/**
	 * Return the separator
	 * @return
	 */
	public String getMultipleFieldSeparator() {
		return this.multipleFieldSeparator;
	}
	
	/**
	 * Stores information about an XML Field (for a mapping to an index field).
	 * @param fieldname
	 * @param type
	 */
	public XMLField(String fieldname, String type) {
		this.fieldname = fieldname;
		this.type = type;
		this.index = Field.Index.ANALYZED;
	}
	
	/**
	 * Stores information about an XML Field (for a mapping to an index field).
	 * @param fieldname
	 * @param type
	 * @param index
	 */
	public XMLField(String fieldname, String type, Field.Index index) {
		this.fieldname = fieldname;
		this.type = type;
		this.index = index;
	}
	
	/**
	 * Stores information about an XML Field (for a mapping to an index field).
	 * @param fieldname
	 * @param type
	 * @param index
	 */
	public XMLField(String fieldname, String type, Field.Index index, String multipleFieldSeparator) {
		this.fieldname = fieldname;
		this.type = type;
		this.index = index;
		this.multipleFieldSeparator = multipleFieldSeparator;
	}
}
