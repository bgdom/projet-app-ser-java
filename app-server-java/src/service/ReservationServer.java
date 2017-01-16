package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import bibliotheque.Bibliotheque;
import bibliotheque.Client;
import bibliotheque.Document;
import bibliotheque.NonInscritException;
import bibliotheque.PasLibreException;
import bibliotheque.ServiceServer;

/**
 * represent an implementation of Service and its job is to
 * listen to reservation connections and manage it
 * @author guydo
 *
 */
public class ReservationServer extends AbstractService implements ServiceServer {
	private static final int PORT = 2500;
	private ServerSocket server;
	private InputWorker input; // to read data
	private OutputWorker output;
	private HashMap<Socket, Client> map; // to store sockets
	private Bibliotheque bi;
	private Checker ck;
	private static final String AUTHENTIFICATION_ACTION = "authentification";
	
	/**
	 * Constructor
	 * @param in the Inputworker which will listen for incoming data after a socket was accepted
	 * @param out the output worker which will write data
	 * @param b the library
	 */
	public ReservationServer(InputWorker in, OutputWorker out, Bibliotheque b, Checker ck){
		try {
			this.ck = ck;
			server = new ServerSocket(PORT);
			input = in;
			output = out;
			this.bi = b;
			map = new HashMap<Socket,Client>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  will be run on a thread
	 */
	@Override
	public void run() {
		if(server != null){// if there was no IOEception, we can start to work
			try {
				System.err.println("Reservation : Marche");
				server.setSoTimeout(1000);
				do{
					try{
						Socket s = server.accept(); // accept connections
						s.setSoTimeout(1);
						Data d = new Data(s,null, this);
						input.add(d); // put data on the inputworker
						d.setMsg(AUTHENTIFICATION_ACTION + System.getProperty("line.separator") + "Vous êtes connecté(e) sur le serveur"
						+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
						output.add(d); // start the communication
						synchronized(map){
							map.put(s, null); // put on the map
							System.err.println("Reservation : Un nouveau client est accepté, il y en a "+ map.size());
						} 
					}catch (SocketTimeoutException e){
						
					}
 				}while(!th.isInterrupted());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finalize();
		}
	}

	@Override
	public boolean consume(Data data) {
		// TODO Auto-generated method stub
		String[] array = data.getMsg().split(System.getProperty("line.separator"));
		if(array.length >= 2){
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < array.length; ++i)
				sb.append(array[i]);
			return matchFunction2(array[0], sb.toString(), data);
		}else
			return matchFunction1(array[0], data);
	}

	private boolean matchFunction1(String a, Data d){
		return false;
	}
	
	private boolean matchFunction2(String a, String b, Data d){
		if(a.startsWith(AUTHENTIFICATION_ACTION)){
			return authentificationAction(a,b,d);
		}
		return false;
	}
	
	private boolean authentificationAction(String a, String b, Data d){
		switch(a){
			case AUTHENTIFICATION_ACTION: // to identifie user
				Client abo = null;
				try{
					abo = bi.getClientById(Integer.valueOf(b)); // get a user by the Id gave
				}catch(NumberFormatException | NonInscritException e){}
				
				if(abo == null){ // if there is no user with the id
					d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+"Vous êtes connecté(e) sur le serveur"
							+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
				} else { // if there is a user associated 
					Client c = retrieveClient(d.getS()); // try to retrieve if a client is already associated with a socket
					StringBuilder sb = new StringBuilder();
					sb.append(AUTHENTIFICATION_ACTION+"Ok" + System.getProperty("line.separator"));
					if(c == null){ // if the user is not already registered in the map
						if(clientAlreadyMapped(abo)){ // if the client is already mapped with a socket
							//System.out.println("abo conflict co");
							d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+
									"Problème d'identité, vous êtes déconnecté, reconnectez vous avec votre Id"+ System.getProperty("line.separator")); 
							return true;
						}
						//System.out.println("new abo co");
						setClient(d.getS(), abo); // registers it with his account
						sb.append("Vous êtes identifié(e)" + System.getProperty("line.separator"));
						authOkAction(sb);
						d.setMsg(sb.toString());
					}else{
						//System.out.println("abo already co");
						authOkAction(sb);
						d.setMsg(sb.toString());
					}
				}
				return true;
				
			case AUTHENTIFICATION_ACTION+"Ok":
				Document doc = null;
				try{
					doc = bi.getDocumentById(Integer.valueOf(b)); // get adocument by the Id gave
				}catch(NumberFormatException | NonInscritException e){}
				StringBuilder sb = new StringBuilder();
				if(doc == null){ // if it doesn't exist
					sb.append(AUTHENTIFICATION_ACTION+"OkErreur" + System.getProperty("line.separator"));
					sb.append("L'id ne correspond pas, entrez en un nouveau "+ System.getProperty("line.separator"));
					authOkAction(sb);
				} else {
					try { // try to book the document
						sb.append(AUTHENTIFICATION_ACTION+"Ok");
						Client c = retrieveClient(d.getS());
						if(!ck.isProblem(c)){
							doc.reserver(c);
							sb.append(System.getProperty("line.separator")+"Votre document est réservé, choisissez en d'autres si vous voulez"+ System.getProperty("line.separator"));
						}else
							sb.append("Erreur" + System.getProperty("line.separator")+"Vous ne pouvez pas réservez"+ System.getProperty("line.separator"));
							authOkAction(sb);
					} catch (PasLibreException e) { // otherwise
						sb.append(System.getProperty("line.separator")+"Ce document n'est pas libre, entrez en un nouveau, un mail vous sera envoyez quand il sera de nouveau disponible"+ System.getProperty("line.separator"));
						authOkAction(sb);
					}
				}
				d.setMsg(sb.toString());
				return true;
			default:
				return false;
		}
	}
	
	private void authOkAction(StringBuilder sb){
		List<Document> l = bi.getDocumentsLibre();
		if(l.size() != 0){
			sb.append("Voici les documents libre (sélectionnez les en entrant leur numéro)"+ System.getProperty("line.separator"));
			for(Document doc : l){
				sb.append(doc.toString() + System.getProperty("line.separator"));
			}
		}else{
			sb.append("Aucun document n'est libre" + System.getProperty("line.separator"));
			sb.append("Entrez à nouveau votre Id pour rafraichir" + System.getProperty("line.separator"));
		}
	}

	private boolean clientAlreadyMapped(Client c){
		boolean yes = false;
		synchronized(map){
			for(Client cl : map.values()){
				if(cl == c){
					yes = true;
					break;
				}
			}
		}
		return yes;
	}
	
	/**
	 * get the client associeted with this socket
	 * @param key
	 * @return null if there is not association or if associated with null
	 */
	private Client retrieveClient(Socket key){
		Client c  = null;
		synchronized(map){
			c = map.get(key);
		}
		return c;
	}
	
	private void setClient(Socket key, Client c){
		synchronized(map){
			map.put(key, c);
		}
	}
	
	@Override
	public void remove(Socket s){
		// TODO Auto-generated method stub
		synchronized(map){
			if(map.containsKey(s)){
				map.remove(s);
				try {
					s.close();
					System.err.println("Un nouveau client est déconnecté, il y en reste "+ map.size());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("IOE during closing");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * disconnect the server if needed and disconnect all sockets if needed
	 * and stop the server
	 */
	@Override
	public void finalize(){
		if(server != null){
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized(map){
				Iterator<Entry<Socket, Client>> it = map.entrySet().iterator();
				while(it.hasNext()){
					try {
						it.next().getKey().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					it.remove();
				}
			}
			System.err.println("Reservation : Arrêt");
		}
	}
}
