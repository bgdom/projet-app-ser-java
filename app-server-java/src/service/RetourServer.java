package service;

import bibliotheque.Bibliotheque;
import bibliotheque.Client;
import bibliotheque.Document;
import bibliotheque.NonInscritException;
import bibliotheque.ServiceServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class RetourServer extends AbstractService implements ServiceServer {
	private static final int PORT = 2700;
	private static final String RETOUR_ACTION = "Retour";
	private ServerSocket server;
	private InputWorker input; // to read data
	private OutputWorker output;
	private HashMap<Socket,Client> map; // to store sockets
	private Bibliotheque bi;
	private Checker ck;
	
	public RetourServer(InputWorker in, OutputWorker out, Bibliotheque b, Checker c) {
		try {
			server = new ServerSocket(PORT);
			input = in;
			output = out;
			this.bi = b;
			ck = c;
			map = new HashMap<Socket,Client>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (server != null) {// if there was no IOException, we can start working
			try {
				System.err.println("RETOUR : Marche");
				server.setSoTimeout(1000);
				do{
					try{
						Socket s = server.accept(); // accept connections
						s.setSoTimeout(1);
						Data d = new Data(s, null, this);
						input.add(d); // put data on the Inputworker
						d.setMsg(RETOUR_ACTION + System.getProperty("line.separator")
								+ "Vous êtes connecté(e) sur le serveur de RETOUR" + System.getProperty("line.separator")
								+ " Entrez L'Id du livre a rendre" + System.getProperty("line.separator"));
						output.add(d); // start the communication
						synchronized (map) {
							map.put(s, null); // put on the map
							System.err.println("Retour : Un nouveau client est accepté, il y en a " + map.size());
						}
					} catch (SocketTimeoutException e){

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
		if (array.length >= 2) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < array.length; ++i)
				sb.append(array[i]);
			return matchFunction2(array[0], sb.toString(), data);
		} else
			return false;
	}

	private boolean matchFunction2(String a, String b, Data d) {
		if (a.startsWith(RETOUR_ACTION)) {
			return retourAction(a, b, d);
		}
		return false;
	}

	private boolean retourAction(String a, String b, Data d) {
		Document doc = null;
		String[] array = b.split(" ");
		
		if(array.length > 1)
			b = array[0];
		try{
			doc = bi.getDocumentById(Integer.valueOf(b));
		} catch(NonInscritException | NumberFormatException e){}
		if (doc != null) {
			if (!bi.isFree(doc)) {
				Client holder = (bi.getEmprunteur(doc) != null) ? bi.getEmprunteur(doc) : bi.getReserver(doc);
				d.setMsg(RETOUR_ACTION + System.getProperty("line.separator") + "Ce livre (" +doc.toString()+") etait emprunté par "
						+ holder.getNom().toUpperCase() + " "
						+ holder.getPrenom().toUpperCase() + System.getProperty("line.separator")
						+ "Le retour du livre " + doc.toString() + " à bien été enregistrer "+  System.getProperty("line.separator"));
				StringBuilder s = new StringBuilder();
				for(Document dc : bi.getEmpruntedBy(holder)){
					if(dc != doc)
					s.append(dc.toString()+ System.getProperty("line.separator"));
				}
				for(Document dc : bi.getReservedBy(holder)){
					if(dc != doc)
					s.append(dc.toString()+ System.getProperty("line.separator"));
				}
				d.setMsg(d.getMsg()+"Voici les livres encore non rendu ou réservés : "+s.toString()
						+ System.getProperty("line.separator") +"Entrez l'id d'un autre livre a rendre :"+ System.getProperty("line.separator"));
				if(array.length > 1){
					ck.addClient(holder);
					d.setMsg(d.getMsg()+"(Vous êtes pénalisés pour la dégradation du livre)"+ System.getProperty("line.separator"));
				} else if (!ck.manageTime(doc))
					d.setMsg(d.getMsg()+"(Vous êtes pénalisés pour le retard du livre)"+ System.getProperty("line.separator"));
				doc.retour();
			} else {
				d.setMsg(RETOUR_ACTION + "Erreur" + System.getProperty("line.separator") + doc.toString()
						+ " n'etait pas emprunter" + System.getProperty("line.separator")
						+ " réessayer avec un autre id" + System.getProperty("line.separator"));
			
			}
		} else {
			d.setMsg(RETOUR_ACTION + "Erreur" + System.getProperty("line.separator") + "Ce livre n'existe pas"
					+ System.getProperty("line.separator") + " réessayer avec un autre id"
					+ System.getProperty("line.separator"));
			
		}
		return true;
	}

	@Override
	public void remove(Socket s){
		// TODO Auto-generated method stub
		synchronized(map){
			if(map.containsKey(s)){
				map.remove(s);
				try {
					s.close();
					System.err.println("Retour : Un nouveau client est déconnecté, il y en reste "+ map.size());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("IOE during closing");
					e.printStackTrace();
				}
			}
		}
	}
	
	
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
			System.err.println("RETOUR : Arrêt");
		}
	}
}
