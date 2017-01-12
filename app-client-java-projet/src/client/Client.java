package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.TimerTask;

import javax.swing.Timer;

/**
 * Classe client
 * 
 * @author hassa
 *
 */
public class Client{
	private Socket s;

	// certification BretteSoft "Guerrier des steppes"
	private Timer session;
	//La dur�e d'une session sans activit� dure 10 minute
	private static final int DUREE_SESSION = 600000;

	public Client(String Host, int Port) throws UnknownHostException, IOException {
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		s = new Socket(Host, Port);
		
		run();
	}

	public void run() {

		try {
			// Debut de la session
			// Gere le temps d'activit� d'un utilisateur
			ActionListener activit� = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					throw new DelaiSessionDepasserException();
				}
			};
			session = new Timer(DUREE_SESSION, activit�);
			session.setRepeats(false);
			session.start();

			// Cree les streams pour lire et ecrire du texte dans cette socket
			BufferedReader sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter sout = new PrintWriter(s.getOutputStream(), true);
			// Cree le stream pour lire du texte a partir du clavier
			// (on pourrait aussi utiliser Scanner)
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
			// Informe l'utilisateur de la connection
			System.out.println("Connect� au serveur " + s.getInetAddress() + ":" + s.getPort());

			String line = "";
			StringBuilder sb = new StringBuilder();
			String l;
			while (true) {

				while((line = sin.readLine()) != null) {
					sb.append(line);
					if(!sin.ready())
						break;
					else
						sb.append(System.getProperty("line.separator"));
				}
				line = sb.toString();
				if(line.contains("Erreur")){
					System.err.println("> ERREUR RECOMMENCER\n" +line);
					System.err.flush();	
				}else if(line.contains("resaOk")){
					line = line.replace("resaOk", "");
					System.out.println("> " +line);
					System.out.flush();	
					break;
				}
				else{
				
					System.out.println("> " +line);
					System.out.flush();	
				}
				l = clavier.readLine();
				if(l.isEmpty()){
					clavier.close();
					break;
				}
				l = getAction(line)+System.getProperty("line.separator")+l;
				// si il ya une activit� de la part de l'utilisateur on
				// reinitialise le timer
				session.restart();

				line = "";
				sb.delete(0, sb.length());
				if (l.equals(""))
					break;
				// envoie au serveur
				sout.println(l);
				sout.flush();

			}
		} catch (IOException e) {
			System.out.println("Connection fermee par le serveur");
		}
		System.out.println("Aurevoir");
		// Refermer dans tous les cas la socket
		try {
			if (s != null)
				s.close();
		} catch (IOException e2) {
			
		}
	}

	/**
	 * Retourne une action contenu dans un message
	 */
	private static String getAction(String line) {
		// TODO Auto-generated method stub
		if(line.contains("Erreur")){
			line =line.replace("Erreur", "");
		}
		return line.substring(0,line.indexOf(System.getProperty("line.separator")));
	 
	}

	/**
	 * Fonction appel�e juste avant que le garbage collector supprime l'objet
	 */
	protected void finalize() {
		if(s != null)
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
