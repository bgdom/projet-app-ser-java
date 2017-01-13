package abonne;

import bibliotheque.Client;

/**
 * 
 * @author guydo
 * represent a User registered
 */
public class Abonne implements Client{
	private String nom;
	private  String prenom;
	private int id;
	private String email;
	
	@Override
	public int hashCode(){
		return id;
	}
	/**
	 * constructor
	 * @param nom
	 * @param prenom
	 * @param id
	 */
	public Abonne(String nom, String prenom, int id,String Email) {
		this.nom = nom;
		this.prenom = prenom;
		this.id = id;
		this.email =Email;
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
	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}
}
