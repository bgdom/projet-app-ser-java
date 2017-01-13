package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * represent the worker where datas are going to be writed onto sockets
 * @author guydo
 *
 */
public class OutputWorker extends AbstractService {
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
			System.err.println("OutputWorker : Marche");
			Data d = null;
			do{
				synchronized(liste){
					if(liste.size() == 0){ // if there is no data to output
							//System.out.println("output waiting");
							liste.wait(); // wait until data is added
							//System.out.println("output woked up");
					}
					d = liste.remove(); // else remove and take it
				}
				try {
					PrintWriter buff = new PrintWriter(d.getS().getOutputStream()); // to write data into the socket
					buff.print(d.getMsg());
					buff.flush();
					//System.out.println(d.getC().getClass().getName() +" : "+d.getMsg());
				} catch (IOException e) {
					d.getC().remove(d.getS()); // if there is a problem, remove and disconnect this socket
					synchronized(liste){
						liste.removeFirstOccurrence(d); // remove from the list
					}
				}
				d = null;
			}while(!th.isInterrupted());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("OutputWorker : Arrêt");
		}
		liste.clear();
	}

}
