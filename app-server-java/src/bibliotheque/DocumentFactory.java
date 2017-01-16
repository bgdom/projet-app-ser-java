package bibliotheque;

import java.util.List;

/**
 * represent an abstract factory to load do documents
 * @author guydo
 *
 */
public interface DocumentFactory {
	
	/**
	 * 
	 * @param fileName the file name where we're going to search
	 * @return a list of documents
	 */
	List<Document> getDocumentFromFile(String fileName);
}
