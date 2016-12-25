package document;

import abonne.Abonne;
import blibliotheque.Document;
import blibliotheque.PasLibreException;
/**
 * 
 * @author guydo
 * represent an implementation of Document, a book 
 */
public class Livre implements Document {
	private int numero;
	private String titre;
	private Abonne reserveur;
	private Abonne emprunteur;

	public Livre(Integer numero, String titre) {
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
	public void reserver(Abonne ab) throws PasLibreException {
		if (reserveur != null)
			throw new PasLibreException();

		this.reserveur = ab;

	}

	@Override
	public void emprunter(Abonne ab) throws PasLibreException {
		if (reserveur != null) {
			if (reserveur.equals(ab)) {
				emprunteur = ab;
			} else {
				throw new PasLibreException();
			}

		}

	}

	@Override
	public void retour() {
		if (reserveur !=null && emprunteur !=null){
			reserveur = null;
			emprunteur = null;
		}
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
		return (emprunteur != null && reserveur != null);
	}
}
