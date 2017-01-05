package abonne;

import java.util.LinkedList;
import java.util.List;

import bibliotheque.Client;
import bibliotheque.Document;

/**
 * 
 * @author guydo
 * represent a User registered
 */
public class Abonne implements Client{
	private String nom;
	private  String prenom;
	private int id;
	private LinkedList<Document> emprunt;
	private LinkedList<Document> reserve;

	/**
	 * constructor
	 * @param nom
	 * @param prenom
	 * @param id
	 */
	public Abonne(String nom, String prenom, int id) {
		this.nom = nom;
		this.prenom = prenom;
		this.id = id;
		emprunt = new LinkedList<Document>();
		reserve = new LinkedList<Document>();
	}
	
	/**
	 * retrieve the last name
	 * @return
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * retrieve the Id
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * retrive the first name
	 * @return
	 */
	public String getPrenom() {
		// TODO Auto-generated method stub
		return prenom;
	}
	
	@Override
	public String toString(){
		return getNom() + " " + getPrenom() + " " + getId();
	}

	@Override
	public void addReserveDocument(Document d) {
		reserve.add(d);
	}

	@Override
	public List<Document> getReservedDocuments() {
		return reserve;
	}

	@Override
	public void removeReserveDoc(Document d) {
		reserve.removeFirstOccurrence(d);
	}

	@Override
	public void removeEmpruntDoc(Document d) {
		// TODO Auto-generated method stub
		emprunt.removeFirstOccurrence(d);
	}

	@Override
	public List<Document> getEmpruntDocuments() {
		// TODO Auto-generated method stub
		return emprunt;
	}

	@Override
	public void addEmpruntDocument(Document d) {
		// TODO Auto-generated method stub
		removeReserveDoc(d);
		emprunt.add(d);
	}
}
