package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

/**
 * represent the worker where datas are going to be readed from sockets
 * @author guydo
 *
 */
public class InputWorker extends AbstractService {
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
			System.err.println("InputWorker : Marche");
			Data d = null;
			do{ // loop over the data list
				synchronized(liste){
					if(liste.size() == 0){ // if the list is empty 
						//System.out.println("input wainting");
							liste.wait(); // wait that an other thread notify after an insert
							//System.out.println("input woked up");
					}
					d = liste.remove(); // get the first data
					liste.add(liste.size(), d); // on the last position
				}
				try {
					BufferedReader buff = new BufferedReader(new InputStreamReader(d.getS().getInputStream())); // to read data
					String line = "";
					StringBuilder sb = new StringBuilder();
					try{
						while((line = buff.readLine()) != null){
							sb.append(line + System.getProperty("line.separator"));
						}
					} catch (SocketTimeoutException e){
						//System.out.println("input time out");
						if(!sb.toString().isEmpty()){
							d.setMsg(sb.toString());
							//System.out.println(d.getC().getClass().getName() +" : "+sb.toString());
							consumer.add(new Data(d.getS(), sb.substring(0), d.getC())); // add to the consumer to process data					
						}
					}
					if(line == null){
						d.getC().remove(d.getS()); // if there is a problem, remove and disconnect it
						synchronized(liste){
							liste.removeFirstOccurrence(d); // remove the data
						}
					}
				} catch (IOException e) {
					d.getC().remove(d.getS()); // if there is a problem, remove and disconnect it
					synchronized(liste){
						liste.removeFirstOccurrence(d); // remove the data
					}
				}
			}while(!th.isInterrupted());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("InputWorker : Arr�t");
		}
		liste.clear();
	}

}
