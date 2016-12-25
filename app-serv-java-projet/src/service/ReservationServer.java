package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import blibliotheque.Bibliotheque;
import blibliotheque.Client;
import blibliotheque.Data;
import blibliotheque.Document;
import blibliotheque.InputWorker;
import blibliotheque.OutputWorker;
import blibliotheque.ServiceServer;

/**
 * represent an implementation of Service and its job is to
 * listen to reservation connections and manage it
 * @author guydo
 *
 */
public class ReservationServer implements ServiceServer {
	private static final int PORT = 2500;
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
	 */
	public ReservationServer(InputWorker in, OutputWorker out, Bibliotheque b){
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
				System.out.println("Le server est en marche");
				do{
					Socket s = server.accept(); // accept connections
					s.setSoTimeout(1);
					Data d = new Data(s,null, this);
					input.add(d); // put data on the inputworker
					d.setMsg(AUTHENTIFICATION_ACTION + System.getProperty("line.separator") + "Vous êtes connecté(e) sur le serveur"
					+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
					output.add(d); // start the communication
					synchronized(map){
						map.put(s, null); // put on the map
						System.out.println("Un nouveau client est accepté, il y en a "+ map.size());
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
		if(a.equals(AUTHENTIFICATION_ACTION)){
			return authentificationAction(a,null,d);
		}
		return false;
	}
	
	private boolean matchFunction2(String a, String b, Data d){
		if(a.equals(AUTHENTIFICATION_ACTION)){
			return authentificationAction(a,b,d);
		}
		return false;
	}
	
	private boolean authentificationAction(String a, String b, Data d){
		Client abo = null;
		if(b != null){
			try{
				abo = bi.getAbonneById(Integer.valueOf(b));
			}catch(NumberFormatException e){}
			
			if(abo == null){
				d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+"Vous êtes connecté(e) sur le serveur"
						+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
				return true;
			}
			else{
				Client c = retrieveClient(d.getS());
				StringBuilder sb = new StringBuilder();
				if(c == null){
					setClient(d.getS(), abo);
					sb.append(AUTHENTIFICATION_ACTION+"Ok" + System.getProperty("line.separator"));
					sb.append("Vous êtes identifié(e)" + System.getProperty("line.separator"));
					List<Document> l = bi.getDocumentsLibre();
					if(l.size() != 0){
						sb.append("Voici les documents libre (sélectionnez les en entrant leur numéro)"+ System.getProperty("line.separator"));
						for(Document doc : l){
							sb.append(doc.getNumero() + " " +doc.getTitre() + System.getProperty("line.separator"));
						}
					}else
						sb.append("Aucun document n'est libre" + System.getProperty("line.separator"));
					d.setMsg(sb.toString());
				}else{
					d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+"Vous êtes connecté(e) sur le serveur"
							+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
				}
				return true;
			}
		}else{
			d.setMsg(AUTHENTIFICATION_ACTION+"Erreur"+System.getProperty("line.separator")+"Vous êtes connecté(e) sur le serveur"
					+ System.getProperty("line.separator") + " Entrez votre Id pour vous identifier" + System.getProperty("line.separator"));
			return true;
		}
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
			if(map.get(s) != null){
				map.remove(s);
				try {
					s.close();
					System.out.println("Un nouveau client est déconnecté, il y en reste "+ map.size());
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
			System.out.println("Le server s'arrête...");
		}
	}
}
