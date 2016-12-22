package blibliotheque;

public class Abonne {
	private String nom;
	private  String prenom;
	private int id;
	public Abonne(String nom, String prenom, int id) {
		this.nom = nom;
		this.prenom = prenom;
		this.id = id;
	}
	public String getNom() {
	return nom;
		
	}
	public int getId() {
		return id;
	}
	public String getPrenom() {
		// TODO Auto-generated method stub
		return prenom;
	}
	
	
}
