package appli;

import java.io.IOException;

import abonne.AbonneLoader;
import blibliotheque.Bibliotheque;
import document.DocumentLoader;
/**
 * 
 * @author guydo
 * the class started by the app
 */
public class AppliServeur {

	public static void main(String[] args) throws IOException {

		new Bibliotheque().start(new DocumentLoader(), new AbonneLoader());
	}

}
