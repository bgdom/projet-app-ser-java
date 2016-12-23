package blibliotheque;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

/**
 * represent the worker where datas are going to be writed onto sockets
 * @author guydo
 *
 */
public class OutputWorker implements Runnable {
	private LinkedList<Data> liste; // list of data
	
	/**
	 * constructor
	 */
	public OutputWorker(){
		liste = new LinkedList<Data>();
	}
	
	/**
	 * add data into the list of data
	 * @param d the data to add
	 */
	public void add(Data d){
		synchronized(liste){
			liste.add(d);
			liste.notify(); // notify thread waiting on this object
		}
	}
	
	/**
	 * will be executed on a thread
	 */
	@Override
	public void run() {
		try {
			Data d = null;
			do{
				synchronized(liste){
					if(liste.size() == 0) // if there is no data to output
							liste.wait(); // wait until data is added
					else
						d = liste.remove(); // else remove and take it
				}
				try {
					BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(d.getS().getOutputStream())); // to write data into the socket
					buff.write(d.getMsg());
					buff.flush();
					System.out.println("Le server a envoyé une réponse");
				} catch (IOException e) {
					d.getC().remove(d.getS()); // if there is a problem, remove and disconnect this socket
					synchronized(liste){
						liste.removeFirstOccurrence(d); // remove from the list
					}
				}
				d = null;
			}while(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
