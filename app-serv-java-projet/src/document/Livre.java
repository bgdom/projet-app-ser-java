package document;

import bibliotheque.Bibliotheque;

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
	private Bibliotheque bi;
	
	public Livre(Integer numero, String titre, Bibliotheque b) {
		bi = b;
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
		bi.reserve(this, ab);
		// Debut de la session de reservation
		ActionListener activité = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				bi.free(Livre.this);
				Livre.this.retour();
			}
		};
		tempsReservation = new Timer(TEMPS_RESERVATION_MAX, activité);
		tempsReservation.setRepeats(false);
		tempsReservation.start();
	}

	@Override
	public void emprunter(Client ab) throws PasLibreException {
		if (reserveur != null) {
			if (reserveur.equals(ab)) {
				// annule le timer de reservation(client est venu l'emprunter a
				// temps)
				tempsReservation.stop();
				tempsReservation = null;
				emprunteur = ab;
				reserveur = null;
				bi.free(this);
				bi.borrow(this, ab);
			} else {
				throw new PasLibreException();
			}

		} else {
			if (emprunteur != null)
				throw new PasLibreException();
			else {
				emprunteur = ab;
				bi.borrow(this, ab);
			}
		}
	}

	@Override
	public void retour() {
		if (reserveur !=null || emprunteur !=null){
			reserveur = null;
			emprunteur = null;
			bi.free(this);

			for (Client a : AvertissementDispo) {
				Mail javaEmail = new Mail();
				try {
					javaEmail.envoyerEmail(
							" Salut " + a.getPrenom() + " ! Le livre " + this.titre
									+ " est disponible Reserver le dès maintenant ou venez l'empruntez.",
							"Information de retour d'un livre ", a.getEmail());
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String toString(){
		return numero() + " " + titre ;
	}

	@Override
	public int hashCode(){
		return titre.hashCode();
	}
}
