package nl.utwente.ir.services.search;

import java.io.File;
import java.io.IOException;

import nl.utwente.ir.services.search.CustomAnalyzer;
import nl.utwente.ir.services.search.Verhaal;
import nl.utwente.ir.services.search.XMLField;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class VerhalenbankIndexer {
	private IndexWriter writer;
	
	public VerhalenbankIndexer() {
		try {
			Directory dir = FSDirectory.open(new File("/home/mark/Downloads/index"));
			Analyzer analyzer = new CustomAnalyzer(Version.LUCENE_31);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31,
					analyzer);
			this.writer = new IndexWriter(dir, iwc);
			writer.getConfig().setOpenMode(OpenMode.CREATE_OR_APPEND);

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}
	
	/**
	 * Indexes the chosen MedlineCitation document
	 * @param citation
	 */
	public void indexDocument(Verhaal document) {
		// make a new, empty document
		Document doc = new Document();
		
		for(XMLField xmlfield : Verhaal.XPATH_FIELDS.values()) {
			if(xmlfield.hasMultipleFields()) {
				for(String value : document.getMultipleValuedField(xmlfield.getFieldname())) {
					Field field = new Field(xmlfield.getFieldname(), value, Field.Store.YES, xmlfield.getIndex());
					doc.add(field);
				}
			} else {
				Field field = new Field(xmlfield.getFieldname(), document.getValue(xmlfield.getFieldname()), Field.Store.YES, xmlfield.getIndex());
				doc.add(field);
			}
		}
		
		System.out.println("updating " + document);
		try {
			writer.updateDocument(new Term("idnummer", document.toString()), doc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the writer
	 */
	public void endIndexing() {
		 try {
			writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
