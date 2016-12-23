package blibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class DataConsumer implements Runnable {
	private OutputWorker output;
	private LinkedList<Data> liste;
	
	public DataConsumer(OutputWorker o){
		output = o;
		liste = new LinkedList<Data>();
	}
	
	public void add(Data d){
		synchronized(liste){
			liste.add(d);
			liste.notify();
		}
	}
	
	@Override
	public void run() {
		try {
			Data d = null;
			do{
				synchronized(liste){
					if(liste.size() == 0)
							liste.wait();
					else{
						d = liste.remove();
					}
				}
				if(d.getC().consume(d)){
					output.add(d);
				}else
					d.getC().remove(d.getS());
				d = null;
			}while(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
