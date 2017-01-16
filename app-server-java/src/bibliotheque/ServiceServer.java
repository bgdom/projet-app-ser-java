package bibliotheque;

import java.net.Socket;

import service.Data;

/**
 * represent a service for the server side
 * @author guydo
 *
 */
public interface ServiceServer {
	
	/**
	 * process and consume the data
	 * @param data the data which will be used
	 * @return if the operation was O.K
	 */
	boolean consume(Data data);
	
	/**
	 * remove a socket from a list/map and disconnect it
	 * @param s the socket to remove
	 */
	void remove(Socket s);
}
