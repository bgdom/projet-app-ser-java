package bibliotheque;

/**
 * represent an abstract document
 * @author guydo
 *
 */
public interface Document {
	/**
	 * 
	 * @return the Id of the Document
	 */
	int numero();
	
	/**
	 * try to reserve a book for a subscriber
	 * @param ab the suscriber
	 * @throws PasLibreException if the Document is'nt free
	 */
	void reserver(Client ab) throws PasLibreException ;
	
	/**
	 * try to borrow a book for a subscriber
	 * @param ab the suscriber
	 * @throws PasLibreException if the Document is'nt free
	 */
	void emprunter(Client ab) throws PasLibreException;
	
	/**
	 * free the document
	 */
	void retour(); // document rendu ou annulation réservation
}
