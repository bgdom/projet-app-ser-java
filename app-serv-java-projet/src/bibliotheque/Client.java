package bibliotheque;

import java.util.List;

public interface Client {
	int getId();
	String getNom();
	String getPrenom();
	void addReserveDocument(Document d);
	void addEmpruntDocument(Document d);
	void removeReserveDoc(Document d);
	void removeEmpruntDoc(Document d);
	List<Document> getReservedDocuments();
	List<Document> getEmpruntDocuments();
	String getEmail();

}
