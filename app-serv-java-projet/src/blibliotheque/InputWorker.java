package blibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class InputWorker implements Runnable {
	private DataConsumer consumer;
	private LinkedList<Data> liste;
	
	public InputWorker(DataConsumer c){
		consumer = c;
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
		// TODO Auto-generated method stub
		try {
			Data d = null;
			do{
				synchronized(liste){
					if(liste.size() == 0)
							liste.wait();
					else{
						d = liste.get(0);
						liste.add(liste.size(), d);
					}
				}
				try {
					if(d.getS().getInputStream().available() > 0){
						System.out.println("Un client a écrit");
						BufferedReader buff = new BufferedReader(new InputStreamReader(d.getS().getInputStream()));
						String line;
						StringBuilder sb = new StringBuilder();
						while((line = buff.readLine()) != null)
							sb.append(System.getProperty("LineSeparator"));
						d.setMsg(sb.toString());
						consumer.add(d);
					}
				} catch (IOException e) {
					d.getC().remove(d.getS());
					synchronized(liste){
						liste.removeFirstOccurrence(d);
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
