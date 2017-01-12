package document;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.Timer;

import bibliotheque.Client;
import bibliotheque.Document;
import bibliotheque.PasLibreException;
import service.Mail;

/**
 * 
 * @author guydo represent an implementation of Document, a book
 */
public class Livre implements Document {
	private int numero;
	private String titre;
	private ArrayList<Client> AvertissementDispo;
	private Client reserveur;
	private Client emprunteur;
	private static final int TEMPS_RESERVATION_MAX = 7200000;
	private Timer tempsReservation;

	public Livre(Integer numero, String titre) {
		this.numero = numero;
		this.titre = titre;
		emprunteur = null;
		reserveur = null;
		AvertissementDispo = new ArrayList<Client>();
	}

	@Override
	public int numero() {
		return numero;
	}

	@Override
	public void reserver(Client ab) throws PasLibreException {
		if (reserveur != null) {
			AvertissementDispo.add(ab);
			throw new PasLibreException();

		} else if (emprunteur != null) {
			AvertissementDispo.add(ab);
			throw new PasLibreException();
		}

		this.reserveur = ab;

		// Debut de la session de reservation
		ActionListener activit� = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				reserveur.removeReserveDoc(Livre.this);
				Livre.this.retour();
			}
		};
		tempsReservation = new Timer(TEMPS_RESERVATION_MAX, activit�);
		tempsReservation.setRepeats(false);
		tempsReservation.start();
		System.out.println("demarer");
	}

	@Override
	public void emprunter(Client ab) throws PasLibreException {
		if (reserveur != null) {
			if (reserveur.equals(ab)) {
				emprunteur = ab;
				ab.addEmpruntDocument(this);
				// annule le timer de reservation(client est venu l'emprunter a
				// temps)
				tempsReservation.stop();
				tempsReservation = null;
			} else {
				throw new PasLibreException();
			}

		} else {
			if (emprunteur != null)
				throw new PasLibreException();
			else {
				emprunteur = ab;
				ab.addEmpruntDocument(this);
			}
		}

	}

	@Override
	public boolean retour() {
		if (reserveur != null || emprunteur != null) {
			reserveur = null;
			emprunteur = null;

			for (Client a : AvertissementDispo) {
				Mail javaEmail = new Mail();
				try {
					javaEmail.envoyerEmail(
							" Salut " + a.getPrenom() + " ! Le livre " + this.titre
									+ " est disponible Reserver le d�s maintenant ou venez l'empruntez.",
							"Information de retour d'un livre ", a.getEmail());
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String getTitre() {
		// TODO Auto-generated method stub
		return titre;
	}

	@Override
	public int getNumero() {
		// TODO Auto-generated method stub
		return numero;
	}

	@Override
	public String toString() {
		return getTitre() + " " + getNumero();
	}

	@Override
	public boolean isFree() {
		// TODO Auto-generated method stub
		return (emprunteur == null && reserveur == null);
	}

	@Override
	public Client getEmprunteur() {
		// TODO Auto-generated method stub
		return emprunteur;
	}
}
