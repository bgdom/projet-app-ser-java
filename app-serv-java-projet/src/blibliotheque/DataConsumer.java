package blibliotheque;

import java.util.LinkedList;

/**
 * 
 * @author guydo
 *class to consume data
 */
public class DataConsumer implements Runnable {
	private OutputWorker output;
	private LinkedList<Data> liste; // liste of data to consume
	
	/**
	 * Contructor
	 * @param o the OutputWorker where data will be writen after its processing
	 */
	public DataConsumer(OutputWorker o){
		output = o;
		liste = new LinkedList<Data>();
	}
	
	/**
	 * add a Data object into the list of Data
	 * @param d the data to add and after consume
	 */
	public void add(Data d){
		synchronized(liste){
			liste.add(d);
			liste.notify();
		}
	}
	
	/**
	 * will be run on a thread
	 */
	@Override
	public void run() {
		try {
			Data d = null;
			do{
				synchronized(liste){
					if(liste.size() == 0) // if there is no data to consume
							liste.wait(); // wait until a data is insert by an other thread
					d = liste.remove(); // else remove it from the list take it
				}
				if(d.getC().consume(d)){ // process it
					output.add(d); // if the processing was O.K, add the result onto the outputworker
				}else
					d.getC().remove(d.getS()); // else, there is a problem, remove and disconnect it
				d = null;
			}while(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
