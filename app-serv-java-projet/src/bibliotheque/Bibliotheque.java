package bibliotheque;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author guydo
 * represent a Library that manage Retour, Emprunt et réservation 
 */
public class Bibliotheque {
	private List<Document> documents;
	private List<Client> abonnes;
	private ConcurrentHashMap<Document, Client> indisponiblesE;
	private ConcurrentHashMap<Document, Client> indisponiblesR;
	
	/**
	 * constructor
	 * @param dFactory factory to load documents
	 * @param aFactory factory to load Users
	 */
	public void start(DocumentFactory dFactory, ClientFactory aFactory,ServiceLoader sl  ){
		documents = dFactory.getDocumentFromFile("listeLivres.txt");
		abonnes = aFactory.getAbonneFromFile("listeAbonne.txt");
		indisponiblesE = new ConcurrentHashMap<Document, Client>();
		indisponiblesR = new ConcurrentHashMap<Document, Client>();
		
		System.out.println(documents);
		System.out.println(abonnes);
		
		List<Service> list = sl.load(this); // load all services associated
		for(Service s : list)
			s.lancer(); // start all
		Scanner in = new Scanner(System.in);
		System.out.println("Appuyez sur entrez pour fermer la bibliothèque");
		in.nextLine();
		for(Service s : list) /* stop all if the user want */
			s.stropper(); 
		in.close();
	}
	
	/**
	 * 
	 * @return the documents list
	 */
	public List<Document> getDocuments(){
		return documents;
	}
	
	/**
	 * 
	 * @return the customers list
	 */
	public List<Client> getClients(){
		return abonnes;
	}
	
	/**
	 * retrieve a user by its id
	 * @param id
	 * @return a user
	 */
	public Client getClientById(int id) throws NonInscritException{
		for (Client a : abonnes) {
			if (id == a.getId()) {
				return a;
			}
		}
		throw new NonInscritException();
	}
	
	/**
	 * retrieve a document by its id
	 * @param id
	 * @return a document
	 */
	public Document getDocumentById(int id) throws NonInscritException {
		for (Document d : documents) {
			if (id == d.numero()) {
				return d;
			}
		}
		throw new NonInscritException();
	}
	
	public List<Document> getDocumentsLibre(){
		List<Document> l = new ArrayList<Document>();
		for (Document d : documents) {
			if (isFree(d)) {
				l.add(d);
			}
		}
		return l;
	}

	public List<Document> getReservedBy(Client c){
		List<Document> l = new ArrayList<Document>();
		if(c == null)
			return l;
		Set<Entry<Document,Client>> set = indisponiblesR.entrySet();
		for(Entry<Document,Client> e : set){
			if(e.getValue() == c)
				l.add(e.getKey());
		}
		return l;
	}
	
	public List<Document> getEmpruntedBy(Client c){
		List<Document> l = new ArrayList<Document>();
		if(c == null)
			return l;
		Set<Entry<Document,Client>> set = indisponiblesE.entrySet();
		for(Entry<Document,Client> e : set){
			if(e.getValue() == c)
				l.add(e.getKey());
		}
		return l;
	}
	
	public Client getEmprunteur(Document doc){
		return indisponiblesE.get(doc);
	}
	public boolean isFree(Document doc) {
		return !indisponiblesE.containsKey(doc) && !indisponiblesR.containsKey(doc);
	}
	
	public void reserve(Document doc, Client c){
		indisponiblesR.put(doc, c);
	}
	
	public void borrow(Document doc, Client c){
		indisponiblesE.put(doc, c);
	}
	
	public void free(Document doc) {
		// TODO Auto-generated method stub
		indisponiblesE.remove(doc);
		indisponiblesR.remove(doc);
	}

	public Client getReserver(Document doc) {
		// TODO Auto-generated method stub
		return indisponiblesR.get(doc);
	}

}
