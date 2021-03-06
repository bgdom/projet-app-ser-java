package service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Classe implementant la certification � Grand Chaman � BretteSoft�
 * @author thameur
 *
 */
public class Mail {
	    Session mailSession;
	    private void setMailServerProperties()
	    {
	        Properties emailProperties = System.getProperties();
	        emailProperties.put("mail.smtp.port", "587");
	        emailProperties.put("mail.smtp.auth", "true");
	        emailProperties.put("mail.smtp.starttls.enable", "true");
	        mailSession = Session.getDefaultInstance(emailProperties, null);
	    }

	    private MimeMessage configurerEmail(String Msgmail,String sujetMail,String Destinataire) throws AddressException, MessagingException
	    {
	       String[] toEmails = {Destinataire};
	      
	      
	        MimeMessage emailMessage = new MimeMessage(mailSession);
	       
	        for (int i = 0; i < toEmails.length; i++)
	        {
	            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
	        }
	        emailMessage.setSubject(sujetMail);
	      
	        emailMessage.setContent(Msgmail, "text/html");
	    
	        return emailMessage;
	    }

	    public void envoyerEmail(String Msgmail,String sujetMail,String Destinataire) throws AddressException, MessagingException
	    {
	    	  this.setMailServerProperties();
	        /**
	         * transmetteur info
	         * */
	    String fromUser = "bibliothequedescartes@gmail.com";
	        String fromUserEmailPassword = "Azerty1234";

	        String emailHost = "smtp.gmail.com";
	        Transport transport = mailSession.getTransport("smtp");
	        transport.connect(emailHost, fromUser, fromUserEmailPassword);
	      
	        /**
	         * Configure
	         * */
	        MimeMessage emailMessage = configurerEmail(Msgmail,sujetMail, Destinataire);
	        /**
	         * envoyer un mail
	         * */
	        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
	        transport.close();
	        System.out.println("Email envoyez avec suc�es");
	    }
	}

