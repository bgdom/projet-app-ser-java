package blibliotheque;

import java.util.List;

public interface DocumentFactory {
	List<Document> getDocumentFromFile(String fileName);
}
