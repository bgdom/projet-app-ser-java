package appli;

import java.io.IOException;

import abonne.AbonneLoader;
import bibliotheque.Bibliotheque;
import document.DocumentLoader;
import service.ServiceManager;
/**
 * 
 * @author guydo
 * the class started by the app
 */
public class AppliServeur {

	public static void main(String[] args) throws IOException {

		Bibliotheque b = new Bibliotheque();
		b.start(new DocumentLoader(b), new AbonneLoader(), new ServiceManager());
	}

}
