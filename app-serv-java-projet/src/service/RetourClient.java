package service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Timer;


public class RetourClient {
	private static final int PORT = 2700;
	private static final String IP = "127.0.0.1";
	private Socket s ;
	// certification BretteSoft "Guerrier des steppes"
		private Timer session;
		//La durée d'une session sans activité dure 3 minute
		private static final int DUREE_SESSION = 180000;

	public RetourClient(){
		
		Socket s = null;
			try {
				s = new Socket(IP, PORT);
				// Debut de la session
				// Gere le temps d'activité d'un utilisateur
				ActionListener activité = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						throw new DelaiSessionDepasserException();
					}
				};
				session = new Timer(DUREE_SESSION, activité);
				session.setRepeats(false);
				session.start();  
				
				// Cree les streams pour lire et ecrire du texte dans cette socket
				BufferedReader sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter sout = new PrintWriter(s.getOutputStream(), true);
				// Cree le stream pour lire du texte a partir du clavier
				// (on pourrait aussi utiliser Scanner)
				BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
				// Informe l'utilisateur de la connection
				System.out.println("Connecté au serveur " + s.getInetAddress() + ":" + s.getPort());

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
					if(l.isEmpty())
						break;
					l = getAction(line)+System.getProperty("line.separator")+l;
					// si il ya une activité de la part de l'utilisateur on
					// reinitialise le timer
					session.restart();
					
					line = "";
					sb.delete(0, sb.length());
					if (l.equals(""))
						break;
					// envoie au serveur
					sout.println(l);
					sout.flush();

					if(line == null)
						System.out.println("Connection fermee par le serveur");
				}
			}

			catch (IOException e) {
				System.out.println("Connection fermee par le serveur");
			}
			System.out.println("Aurevoir");
			// Refermer dans tous les cas la socket
			try {
				if (s != null)
					s.close();
			} catch (IOException e2) {
				;
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

	}
	

