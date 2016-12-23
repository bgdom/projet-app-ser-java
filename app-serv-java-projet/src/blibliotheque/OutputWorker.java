package blibliotheque;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class OutputWorker implements Runnable {
	private LinkedList<Data> liste;
	
	public OutputWorker(){
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
					else
						d = liste.remove();
				}
				try {
					BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(d.getS().getOutputStream()));
					buff.write(d.getMsg());
					buff.flush();
					System.out.println("Le server a envoyé une réponse");
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
