package blibliotheque;

import abonne.Abonne;

public interface Document {

		int numero();
		void reserver(Abonne ab) throws PasLibreException ;
		void emprunter(Abonne ab) throws PasLibreException;
		void retour(); // document rendu ou annulation r�servation
		String getTitre();
		int getNumero();

}
