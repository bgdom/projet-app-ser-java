import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.print.DocFlavor.INPUT_STREAM;

public class Appli {
public static void main(String[] args) throws IOException {
	Socket laSocket = new Socket("127.0.0.1", 1000); 
	BufferedReader socketIn = new BufferedReader(new InputStreamReader(laSocket.getInputStream()));  
	PrintWriter socketOut = new PrintWriter(laSocket.getOutputStream( ),true) ;
	/* bonjour */   
	System.out.println("Bienvenue sur votre système de réservation : ");    
	System.out.println("Vous pouvez ici réserver un livre disponible ");   
	System.out.println("et passer le chercher dans les 2 heures"); 
	/* saisie des données  */   
	Scanner clavier = new Scanner(System.in);    
	System.out.println("Votre numéro d'abonné, svp :");   
	int numAbonné = clavier.nextInt();   
	socketOut.println(numAbonné);  
	System.out.println(socketIn.readLine());
	System.out.println("Le numéro de livre que vous souhaitez réserver :");   
	int numDocument = clavier.nextInt();  /* envoi des données au service */    
	
	socketOut.println(numDocument);
	/* réception de la réponse    * et affichage de cette réponse */  
	System.out.println(socketIn.readLine());  // fermeture de la connexion    
	laSocket.close();  
}
}
