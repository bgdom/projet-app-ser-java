package bibliotheque;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import abonne.Abonne;
import service.EmpruntClient;
import service.EmpruntServer;
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
	public Client getAbonneById(int id) throws NonInscritException{
		for (Client a : abonnes) {
			if (id == a.getId()) {
				return a;
			}
		}
		throw new NonInscritException();
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
	public Document getDocumentById(int id) throws NonInscritException {
		for (Document d : documents) {
			if (id == d.getNumero()) {
				return d;
			}
		}
		throw new NonInscritException();
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
		new Thread(new EmpruntServer(input, output, this)).start();
		
		boolean again = true;
		do{
			System.out.println("Que voulez vous lancez : "+System.getProperty("line.separator")+
					"1. emprunt"+System.getProperty("line.separator")+ "2. réservation"
					+System.getProperty("line.separator")+"3. Tout arreter");
			Scanner in = new Scanner(System.in);
			String line = "";
			boolean again2 = true;
			int code = 0;
			while(again2){
				line = in.nextLine();
				try{
					code = Integer.valueOf(line);
					if(code > 0 && code <= 3)
						again2 = false;
					else
						System.out.println("Votre saisie n'est pas bonnne, recommencez");
				} catch(NumberFormatException e){
					System.out.println("Votre saisie n'est pas bonnne, recommencez");
				}
			}
			switch(code){
			case 1:
				new EmpruntClient();
				break;
			case 3:
				again = false;
				in.close();
				System.out.println("au revoir");
				break;
			}
		}while(again);
	}

}
