package blibliotheque;

import java.util.ArrayList;
import java.util.List;

import abonne.Abonne;

public class Bibliotheque {
	private List<Document> documents;
	private List<Abonne> abonnes;
	
	public void start(DocumentFactory dFactory, AbonneFactory aFactory){
		documents = dFactory.getDocumentFromFile("listeLivres.txt");
		abonnes = aFactory.getAbonneFromFile("listeAbonne.txt");
		System.out.println(documents);
		System.out.println(abonnes);
	}
	
	
	private Abonne getAbonneById(int id) {
		for (Abonne a : abonnes) {
			if (id == a.getId()) {
				return a;
			}
		}
		return null;
	}

	private Document getDocumentById(int id) {
		for (Document d : documents) {
			if (id == d.getNumero()) {
				return d;
			}
		}
		return null;
	}

	/*@Override
	public void run() {

		try {
			client = server.accept();
			System.out.println("Nouveau client : " + this.client.getInetAddress());
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			out.println(
					"Bienvenue sur votre système de réservation : Vous pouvez ici réserver un livre disponible et passer le chercher dans les 2 heures Votre numéro d'abonné, svp :");
			while (true) {

				// lit la ligne
				String line = in.readLine();
				System.out.println("Service " + " a recu " + line);

				// Si cette ligne est vide, le serviceh se termine
				if (line == null)
					break;
				if (line.equals(""))
					break;
				// sinon l'ecrit a l'envers
				Abonne a = getAbonneById(Integer.valueOf(line));
				if (a != null) {
					out.println("Bonjour " + a.getPrenom() + " ravi de vous revoir");
					out.println("Le numéro de livre que vous souhaitez réserver :");
					Document d = getDocumentById(Integer.valueOf(line));

					if (d != null) {
						out.println("Le Documents que vous voullez reserver est " + d.getTitre() + " ? '(oui/non)");

						if (line.equals("oui")) {
							d.reserver(a);
						}

					} else {
						throw new IOException("Livre non reconnu");
					}
				} else {
					throw new NonInscritException();

				}

			}

		} catch (IOException | NonInscritException | PasLibreException e) {
			try {
				this.server.close();
			} catch (IOException e1) {
			}
			System.err.println("Pb sur le port d'écoute :" + e);
		}
	}*/

}
