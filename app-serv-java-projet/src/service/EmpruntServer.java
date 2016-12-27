package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import bibliotheque.Bibliotheque;
import bibliotheque.Client;
import bibliotheque.Data;
import bibliotheque.Document;
import bibliotheque.InputWorker;
import bibliotheque.NonInscritException;
import bibliotheque.OutputWorker;
import bibliotheque.PasLibreException;
import bibliotheque.ServiceServer;

/**
 * represent an implementation of Service and its job is to
 * listen emrpunt connections and manage it
 * @author guydo
 *
 */
public class EmpruntServer implements ServiceServer {
	private static final int PORT = 2600;
	private static final String IP = "127.0.0.1";
	private ServerSocket server;
	private InputWorker input; // to read data
	private OutputWorker output;
	private HashMap<Socket, Client> map; // to store sockets
	private Bibliotheque bi;
	private static final String AUTHENTIFICATION_ACTION = "authentification";
	/**
	 * contructor
	 * @param in the inputworker which will listen for incomming data after a socket was accepted
	 * @param out the output worker which will write data
	 * @param b the library
	 */
	public EmpruntServer(InputWorker in, OutputWorker out, Bibliotheque b){
		try {
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
				System.out.println("Le server d'emprunt est en marche");
				do{
					Socket s = server.accept(); // accept connections
					s.setSoTimeout(1);
					Data d = new Data(s,null, this);
					input.add(d); // put data on the inputworker
					d.setMsg(AUTHENTIFICATION_ACTION + System.getProperty("line.separator") + "Vous êtes connecté(e) sur le serveur d'emprunt"
					+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifiez " + System.getProperty("line.separator"));
					output.add(d); // start the communication
					synchronized(map){
						map.put(s, null); // put on the map
						System.err.println("Emprunt : Un nouveau client est accepté, il y en a "+ map.size());
					}
				}while(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
					abo = bi.getAbonneById(Integer.valueOf(b)); // get a user by the Id gave
				}catch(NumberFormatException| NonInscritException e){}
				if(abo == null){ // if there is no user
					d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+"Vous êtes connecté(e) sur le serveur"
							+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
				} else { // if there is a user
					Client c = retrieveClient(d.getS());
					StringBuilder sb = new StringBuilder();
					sb.append(AUTHENTIFICATION_ACTION+"Ok" + System.getProperty("line.separator"));
					if(c == null){ // if the user is not already registered in the map
						if(clientAlreadyMapped(abo)){
							//System.out.println("abo conflict co");
							d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+
									"Problème d'identité, vous êtes déconnecté, reconnectez vous avec votre Id"+ System.getProperty("line.separator")); 
							return true;
						}
						//System.out.println("Emprunt : new abo co");
						setClient(d.getS(), abo); // registers it with his account
						sb.append("Vous êtes identifié(e)" + System.getProperty("line.separator"));
						authOkAction(sb, retrieveClient(d.getS()));
						d.setMsg(sb.toString());
					}else{
						//System.out.println("Emprunt : abo already co");
						authOkAction(sb, retrieveClient(d.getS()));
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
					authOkAction(sb, retrieveClient(d.getS()));
				} else {
					try { // try to book the document
						sb.append(AUTHENTIFICATION_ACTION+"Ok" + System.getProperty("line.separator"));
						doc.emprunter(retrieveClient(d.getS()));
						sb.append("Votre document est emprunté, choisissez en d'autres si vous voulez"+ System.getProperty("line.separator"));
						authOkAction(sb, retrieveClient(d.getS()));
					} catch (PasLibreException e) { // otherwise
						sb.append("Ce document n'est pas libre, entrez en un nouveau"+ System.getProperty("line.separator"));
						authOkAction(sb, retrieveClient(d.getS()));
					}
				}
				d.setMsg(sb.toString());
				return true;
			default:
				return false;
		}
	}
	
	private void authOkAction(StringBuilder sb, Client c){
		List<Document> l = bi.getDocumentsLibre();
		
		if(l.size() != 0){
			sb.append("Voici les documents libre (sélectionnez les en entrant leur numéro)"+ System.getProperty("line.separator"));
			for(Document doc : l){
				sb.append(doc.getNumero() + " " +doc.getTitre() + System.getProperty("line.separator"));
			}
		}else{
			sb.append("Aucun document n'est libre" + System.getProperty("line.separator"));
		}
		
		l = c.getReservedDocuments();
		if(l.size() != 0){
			sb.append("Voici les documents que vous avez réserver  (sélectionnez les en entrant leur numéro)"+ System.getProperty("line.separator"));
			for(Document doc : l){
				sb.append(doc.getNumero() + " " +doc.getTitre() + System.getProperty("line.separator"));
			}
		}
	}

	private boolean clientAlreadyMapped(Client c){
		synchronized(map){
			for(Client cl : map.values()){
				if(cl == c)
					return true;
			}
		}
		return false;
	}
	
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
					System.err.println("Emprunt : Un nouveau client est déconnecté, il y en reste "+ map.size());
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
			System.err.println("Enprunt : Le server s'arrête...");
		}
	}
}
