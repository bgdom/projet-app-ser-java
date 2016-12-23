package blibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * represent the worker where datas are going to be readed from sockets
 * @author guydo
 *
 */
public class InputWorker implements Runnable {
	private DataConsumer consumer;
	private LinkedList<Data> liste; // list 
	
	/**
	 * constructer
	 * @param c the consumer where data will be processed
	 */
	public InputWorker(DataConsumer c){
		consumer = c;
		liste = new LinkedList<Data>();
	}
	
	/**
	 * add a Data object to the list of Data
	 * @param d the data to add and after read it
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
		// TODO Auto-generated method stub
		try {
			Data d = null;
			do{ // loop over the data list
				synchronized(liste){
					if(liste.size() == 0) // if the list is empty 
							liste.wait(); // wait that an other thread notify after an insert
					else{
						d = liste.get(0); // get the first data
						liste.add(liste.size(), d); // on the last position
					}
				}
				try {
					if(d.getS().getInputStream().available() > 0){ // if there is an input for thos socket
						System.out.println("Un client a écrit");
						BufferedReader buff = new BufferedReader(new InputStreamReader(d.getS().getInputStream())); // to read data
						String line;
						StringBuilder sb = new StringBuilder();
						while((line = buff.readLine()) != null)
							sb.append(System.getProperty("LineSeparator"));
						d.setMsg(sb.toString());
						consumer.add(d); // add to the consumer to process data
					}
				} catch (IOException e) {
					d.getC().remove(d.getS()); // if there is a problem, remove and disconnect it
					synchronized(liste){
						liste.removeFirstOccurrence(d); // remove the data
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
