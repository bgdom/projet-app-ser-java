package appli;

import java.io.IOException;

import blibliotheque.Bibliotheque;

public class AppliServeur {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Bibliotheque(1000).start();
	}

}
