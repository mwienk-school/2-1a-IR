package nl.utwente.ir.parser;

import java.io.File;
import java.io.IOException;

import nl.utwente.ir.analysis.CustomAnalyzer;

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

/**
 * An indexer for the Medline documents
 * @author mark
 *
 */
public class MedlineIndexer {
	private IndexWriter writer;
	
	/**
	 * An indexer for the Medline documents (uses CustomAnalyzer)
	 * @throws IOException
	 */
	public MedlineIndexer() throws IOException {
		try {
			Directory dir = FSDirectory.open(new File("index"));
			Analyzer analyzer = new CustomAnalyzer(Version.LUCENE_31);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31,
					analyzer);
			this.writer = new IndexWriter(dir, iwc);

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}
	
	/**
	 * Indexes the chosen MedlineCitation document
	 * @param citation
	 */
	public void indexDocument(MedlineCitation citation) {
		// make a new, empty document
		Document doc = new Document();
		
		for(XMLField xmlfield : MedlineCitation.XPATH_FIELDS.values()) {
			Field field = new Field(xmlfield.getFieldname(), citation.getValue(xmlfield.getFieldname()), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
			doc.add(field);
		}
		
		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			// New index, so we just add the document (no old document can be there):
			System.out.println("adding " + citation);
			try {
				writer.addDocument(doc);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Existing index (an old copy of this document may have been indexed) so 
			// we use updateDocument instead to replace the old one matching the exact 
			// path, if present:
			System.out.println("updating " + citation);
			try {
				writer.updateDocument(new Term("PMID", citation.toString()), doc);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Close the writer
	 */
	public void endIndexing() {
		 try {
			writer.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
