package blibliotheque;

import java.util.ArrayList;
import java.util.List;


import abonne.Abonne;
import service.ReservationServer;
/**
 * 
 * @author guydo
 * represent a Library that manage Retour, Emprunt et réservation 
 */
public class Bibliotheque {
	private List<Document> documents;
	private List<Client> abonnes;
	
	/**
	 * constructor
	 * @param dFactory factory to load documents
	 * @param aFactory factory to load Users
	 */
	public void start(DocumentFactory dFactory, AbonneFactory aFactory){
		documents = dFactory.getDocumentFromFile("listeLivres.txt");
		abonnes = aFactory.getAbonneFromFile("listeAbonne.txt");
		
		System.out.println(documents);
		System.out.println(abonnes);
		
		run();
	}
	
	/**
	 * retrieve a user by its id
	 * @param id
	 * @return a user
	 */
	public Client getAbonneById(int id) {
		for (Client a : abonnes) {
			if (id == a.getId()) {
				return a;
			}
		}
		return null;
	}
	
	public List<Document> getDocumentsLibre(){
		List<Document> l = new ArrayList<Document>();
		for (Document d : documents) {
			if (d.isFree()) {
				l.add(d);
			}
		}
		return l;
	}
	/**
	 * retrieve a document by its id
	 * @param id
	 * @return a document
	 */
	public Document getDocumentById(int id) {
		for (Document d : documents) {
			if (id == d.getNumero()) {
				return d;
			}
		}
		return null;
	}

	/**
	 * launch services, workers, consumer and start processing
	 */
	private void run() {
		OutputWorker output = new OutputWorker(); // to write data over sockets
		new Thread(output).start();
		
		DataConsumer dataConsumer = new DataConsumer(output); // to consume data that socket inputed
		new Thread(dataConsumer).start();
		
		InputWorker input = new InputWorker(dataConsumer); // to read data over sockets
		new Thread(input).start();
		
		
		new Thread(new ReservationServer(input, output, this)).start(); // Reservation service 
	}

}
