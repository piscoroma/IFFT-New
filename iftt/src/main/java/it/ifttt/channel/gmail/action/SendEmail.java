package it.ifttt.channel.gmail.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.social_api_creators.GmailCreator;

@Component
public class SendEmail implements ActionPerformer {

	/*
	 * Action send-email
	 * 
	 * Ingredienti suportati:
	 * - TO: email destinatario
	 * - [CC]: eventuali altri destinatari
	 * - SUBJECT: oggetto dell'email
	 * - BODY: corpo dell'email
	 * - REPLY_TO: se specificata, manda l'email in risposta a quell'indirizzo.
	 * 
	 */
	
	public static final String TO_KEY = "to";
	public static final String SUBJECT_KEY = "subject";
	public static final String BODY_KEY = "body";
	public static final String REPLY_TO_KEY = "reply-to";
  
	@Autowired
	GmailCreator gmailCreator;
	
	private final static Logger log = Logger.getLogger(SendEmail.class);

	private User user;
	private Map<String, String> userIngredients;
	
	@Override
	public void setUser(User user) {
		this.user = user;	
	}

	@Override
	public void setUserIngredients(List<Ingredient> userIngredientsList) {
		userIngredients = new HashMap<String, String>();
		for(Ingredient ingr : userIngredientsList)
			userIngredients.put(ingr.getName(), ingr.getValue());	
	}

	@Override
	public void perform() throws UnauthorizedChannelException, GeneralSecurityException, IOException, MessagingException{
		log.debug("ACTION: i'm sendEmail");
		log.debug("user: " + user.toString());
		log.debug(userIngredients.toString());
		
		// get main ingredients
		String to = userIngredients.get(TO_KEY); 
		String subject = userIngredients.get(SUBJECT_KEY); 
		String body = userIngredients.get(BODY_KEY);
		String replyTo = null;
		if (userIngredients.containsKey(REPLY_TO_KEY))
			replyTo = userIngredients.get(REPLY_TO_KEY);
		
		// get gmail API for user
		Gmail gmail = gmailCreator.getGmail(user.getUsername());
		
		// create email
		String from = gmail.users().getProfile("me").execute().getEmailAddress();
		MimeMessage mime = createEmail(to, from, subject, body);
		if (replyTo != null)
			mime.addHeader("Reply-To", replyTo);
		Message message = createMessageWithEmail(mime);


		// send email
        message = sendMessage(gmail, from, mime);
        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        
        System.out.println("Performed action 'send-email' of channel Gmail.");
		
	}
	
	/**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    private MimeMessage createEmail(String to, String from, String subject, String bodyText) 
    		throws MessagingException {
        
    	Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }
    
    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    private Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        
    	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
    
    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    private Message sendMessage(Gmail service, String userId, MimeMessage emailContent)
            throws MessagingException, IOException {
       
    	Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();
        return message;
    }

	

}
