package document;

import abonne.Abonne;
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

	public Livre(Integer numero, String titre) {
		this.numero = numero;
		this.titre = titre;
		emprunteur =null /*new Abonne("Thameur","Hassan",1)*/;
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
		
	}

	@Override
	public void emprunter(Client ab) throws PasLibreException {
		if (reserveur != null) {
			if (reserveur.equals(ab)) {
				emprunteur = ab;
				ab.addEmpruntDocument(this);
			} else {
				throw new PasLibreException();
			}

		} else {
			if(emprunteur != null)
				throw new PasLibreException();
			else{
				emprunteur = ab;
				ab.addEmpruntDocument(this);
			}
		}

	}

	@Override
	public boolean retour() {
		if (reserveur !=null || emprunteur !=null){
			reserveur = null;
			emprunteur = null;
			return true;
		}
		return false;
	}

	@Override
	public String getTitre() {
		// TODO Auto-generated method stub
		return titre;
	}

	@Override
	public int getNumero() {
		// TODO Auto-generated method stub
		return numero;
	}
	
	@Override
	public String toString(){
		return getTitre() +" " + getNumero() ;
	}

	@Override
	public boolean isFree() {
		// TODO Auto-generated method stub
		return (emprunteur == null && reserveur == null);
	}

	@Override
	public Client getEmprunteur() {
		// TODO Auto-generated method stub
		return reserveur;
	}
}
