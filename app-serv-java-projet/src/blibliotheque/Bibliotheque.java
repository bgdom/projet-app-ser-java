package blibliotheque;

import java.util.List;

import abonne.Abonne;
import service.ReservationServer;

public class Bibliotheque {
	private List<Document> documents;
	private List<Abonne> abonnes;
	
	public void start(DocumentFactory dFactory, AbonneFactory aFactory){
		documents = dFactory.getDocumentFromFile("listeLivres.txt");
		abonnes = aFactory.getAbonneFromFile("listeAbonne.txt");
		
		
		System.out.println(documents);
		System.out.println(abonnes);
		
		run();
	}
	
	
	private Abonne getAbonneById(int id) {
		for (Abonne a : abonnes) {
			if (id == a.getId()) {
				return a;
			}
		}
		return null;
	}

	private Document getDocumentById(int id) {
		for (Document d : documents) {
			if (id == d.getNumero()) {
				return d;
			}
		}
		return null;
	}

	private void run() {
		OutputWorker output = new OutputWorker(); // to write data over sockets
		new Thread(output).start();
		
		DataConsumer dataConsumer = new DataConsumer(output); // to consume data that socket inputed
		new Thread(dataConsumer).start();
		
		InputWorker input = new InputWorker(dataConsumer); // to read data over sockets
		new Thread(input).start();
		
		
		new Thread(new ReservationServer(input)).start(); // Reservation service 
	}

}
