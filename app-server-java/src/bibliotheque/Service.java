package bibliotheque;

public interface Service extends Runnable{
	/**
	 * start the service
	 */
	void lancer();
	
	/**
	 * stop the service
	 */
	void stropper();
}
