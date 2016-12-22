import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Appli {
public static void main(String[] args) {
	Socket laSocket = new Socket("127.0.0.1", 5000); 
	BufferedReader socketIn = new BufferedReader(in);  
	PrintWriter socketOut = ...; 
	/* bonjour */   
	System.out.println("Bienvenue sur votre système de réservation : ");    
	System.out.println("Vous pouvez ici réserver un livre disponible ");   
	System.out.println("et passer le chercher dans les 2 heures"); 
	/* saisie des données  */   
	Scanner clavier = new Scanner(System.in);    
	System.out.println("Votre numéro d'abonné, svp :");   
	int numAbonné = clavier.nextInt();   
	System.out.println("Le numéro de livre que vous souhaitez réserver :");   
	int numDocument = clavier.nextInt();  /* envoi des données au service */    
	socketOut.println(numAbonné);  
	socketOut.println(numDocument);
	/* réception de la réponse    * et affichage de cette réponse */  
	System.out.println(socketIn.readLine());  // fermeture de la connexion    
	laSocket.close();  
}
}
