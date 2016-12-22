package blibliotheque;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import abonne.AbonneLoader;
import document.DocumentLoader;

public class Bibliotheque implements Runnable {
	private ArrayList<Document> documents;
	private ArrayList<Abonne> abonnes;
	private ServerSocket server;
	public Bibliotheque(int port) throws IOException{
		server= new ServerSocket(port);
		documents = new DocumentLoader().getDocumentFromFile("listeLivres.txt");
		abonnes = new AbonneLoader().getAbonneFromFile("listeAbonne.txt");
	}
	
	public void start(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			while(true)
				server.accept();
		}
		catch (IOException e) { 
			try {this.server.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}
	
	
}
