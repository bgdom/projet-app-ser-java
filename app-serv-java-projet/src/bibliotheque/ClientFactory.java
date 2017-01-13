package bibliotheque;

import java.util.List;

public interface ClientFactory {
	
	/**
	 * 
	 * @param fileName the file name where we're going to search
	 * @return a list of Users
	 */
	List<Client> getAbonneFromFile(String fileName);
}
