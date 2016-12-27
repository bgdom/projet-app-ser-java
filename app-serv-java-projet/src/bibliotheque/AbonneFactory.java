package bibliotheque;

import java.util.List;

import abonne.Abonne;

public interface AbonneFactory {
	
	/**
	 * 
	 * @param fileName the file name where we're going to search
	 * @return a list of Users
	 */
	List<Client> getAbonneFromFile(String fileName);
}
