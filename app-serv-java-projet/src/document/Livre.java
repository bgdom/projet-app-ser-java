package document;

import bibliotheque.Bibliotheque;
import bibliotheque.Client;
import bibliotheque.Document;
import bibliotheque.PasLibreException;
/**
 * 
 * @author guydo
 * represent an implementation of Document, a book 
 */
public class Livre implements Document {
	private int numero;
	private String titre;
	private Client reserveur;
	private Client emprunteur;
	private Bibliotheque bi;
	
	public Livre(Integer numero, String titre, Bibliotheque b) {
		bi = b;
		this.numero = numero;
		this.titre = titre;
		emprunteur = null;
		reserveur = null;
	}

	@Override
	public int numero() {
		return numero;
	}

	@Override
	public void reserver(Client ab) throws PasLibreException {
		if (reserveur != null)
			throw new PasLibreException();
		else if(emprunteur != null)
			throw new PasLibreException();
		this.reserveur = ab;
		bi.reserve(this, ab);
	}

	@Override
	public void emprunter(Client ab) throws PasLibreException {
		if (reserveur != null) {
			if (reserveur.equals(ab)) {
				emprunteur = ab;
				reserveur = null;
				bi.free(this);
				bi.borrow(this, ab);
			} else {
				throw new PasLibreException();
			}

		} else {
			if(emprunteur != null)
				throw new PasLibreException();
			else{
				emprunteur = ab;
				bi.borrow(this, ab);
			}
		}
	}

	@Override
	public void retour() {
		if (reserveur !=null || emprunteur !=null){
			reserveur = null;
			emprunteur = null;
			bi.free(this);
		}
	}
	
	@Override
	public String toString(){
		return numero() + " " + titre ;
	}

	@Override
	public int hashCode(){
		return titre.hashCode();
	}
}
