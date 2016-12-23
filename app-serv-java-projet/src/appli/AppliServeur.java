package appli;

import java.io.IOException;

import abonne.AbonneLoader;
import blibliotheque.Bibliotheque;
import document.DocumentLoader;

public class AppliServeur {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Bibliotheque().start(new DocumentLoader(), new AbonneLoader());
	}

}
