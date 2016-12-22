package appli;

import blibliotheque.Bibliotheque;
import document.DocumentLoader;

public class AppliServeur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Bibliotheque().start(new DocumentLoader());
	}

}
