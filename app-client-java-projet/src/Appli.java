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
	System.out.println("Bienvenue sur votre syst�me de r�servation : ");    
	System.out.println("Vous pouvez ici r�server un livre disponible ");   
	System.out.println("et passer le chercher dans les 2 heures"); 
	/* saisie des donn�es  */   
	Scanner clavier = new Scanner(System.in);    
	System.out.println("Votre num�ro d'abonn�, svp :");   
	int numAbonn� = clavier.nextInt();   
	System.out.println("Le num�ro de livre que vous souhaitez r�server :");   
	int numDocument = clavier.nextInt();  /* envoi des donn�es au service */    
	socketOut.println(numAbonn�);  
	socketOut.println(numDocument);
	/* r�ception de la r�ponse    * et affichage de cette r�ponse */  
	System.out.println(socketIn.readLine());  // fermeture de la connexion    
	laSocket.close();  
}
}
