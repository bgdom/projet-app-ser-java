package blibliotheque;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
	private List<Document> documents;
	private ArrayList<Abonne> abones;
	
	public void start(DocumentFactory factory){
		documents = factory.getDocumentFromFile("listeLivres.txt");
		System.out.println(documents);
		
	}
	
	private ArrayList<Abonne> getAbonnesFromFile(String fileName){
		return null;
	}
}
