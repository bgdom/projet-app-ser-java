package testmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class test {
public static void main(String[] args) {

	/* L'adresse IP de votre serveur SMTP */
	String smtpServer = "smtp.gmail.com";

	/* L'adresse de l'exp�diteur */
	String from = "hassan.thameur@gmail.com";

	/* L'adresse du destinataire */
	String to = "hassan.thameur@gmail.com";

	/* L'objet du message */
	String objet = "Salut";

	/* Le corps du mail */
	String texte = "Texte du mail";

	Properties props = System.getProperties();
	props.put("mail.transport.protocol", "smtp");
	props.put("mail.smtp.host", smtpServer);
	props.put("mail.smtp.port", "25");
	/* Session encapsule pour un client donn� sa connexion avec le serveur de mails.*/
	Session session = Session.getDefaultInstance(props, null);

	/* Cr�ation du message*/
	Message msg = new MimeMessage(session);

	try {
	      msg.setFrom(new InternetAddress(from));
	      msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
	      msg.setSubject(objet);
	      msg.setText(texte);
	      msg.setHeader("X-Mailer", "LOTONtechEmail");
	      Transport.send(msg);
	}
	catch (AddressException r) {
	      r.printStackTrace();
	} 
	catch (MessagingException e) {
	      e.printStackTrace();
	}
}
}