package it.ifttt.channel.gmail.trigger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Profile;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gmail.action.SendEmail;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.social_api_creators.GmailCreator;

@Component
public class MailReceivedEvent implements TriggerEvent {

	/*
	 * Trigger: mail-received
	 * 
	 * Ingredienti supportati:
	 * - FROM: il trigger si scatena solo se il mittente dell'email coincide con questo
	 * - TO: il trigger si scatena solo se il destinatario dell'email coincide con questo
	 * - CC: lista di cc separati da spazi
	 * - SUBJECT: il trigger si scatena solo se l'oggetto dell'email CONTIENE questo testo
	 * - BODY: il trigger si scatena solo se il testo dell'email CONTIENE questo testo
	 * 
	 * L'evento che si genera se il trigger è verificato contiene i seguenti elementi:
	 * - FROM: mittente dell'email
	 * - TO destinatario dell'email
	 * - SUBJECT: oggetto dell'email
	 * - BODY: testo dell'email
	 * - CC: eventuali altri destinatari
	 * - DATE: data di ricezione dell'email
	 * - FROM_NAME: nome del mittente
	 * - TO_NAME: nome del destinatario
	 * 
	 * Questi e tutti gli altri parametri vengono iniettati come ingredienti nell'action.
	 */
	
	public static final String FROM_KEY = "from";
	public static final String TO_KEY = "to";
	public static final String SUBJECT_KEY = "subject";
	public static final String BODY_KEY = "body";
	public static final String CC_KEY = "cc";
	public static final String DATE_KEY = "date";
	public static final String FROM_NAME_KEY = "from-name";
	public static final String TO_NAME_KEY = "to-name";
	
	@Autowired
	GmailCreator gmailCreator;
	
	private final static Logger log = Logger.getLogger(SendEmail.class);
	
	private User user;
	private Date lastRefresh;
	private Map<String, String> userIngredients;
    
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setLastRefresh(Date lastRefresh) {
		this.lastRefresh = lastRefresh;
	}
	
	@Override
	public Date getLastRefresh() {
		return lastRefresh;
	}

	@Override
	public void setUserIngredients(List<Ingredient> ingredients) {
		userIngredients = new HashMap<String, String>();
		for(Ingredient ingr : ingredients)
			userIngredients.put(ingr.getName(), ingr.getValue());
	}

	@Override
	public List<Object> raise() throws Exception {
		
		List<Object> events = new ArrayList<Object>();
		Message email;
		log.debug("-->richiesta a gmail");
		while ((email = getNextEmail()) != null) {
			events.add((Object)email);
		}
		
		this.lastRefresh = new Date();
		return events;
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		Message message = (Message)obj;
		
		for(Ingredient ingr : injeactableIngredient){
			switch(ingr.getName()){
			case FROM_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(FROM_KEY)));
				break;
			case TO_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(TO_KEY)));
				break;
			case SUBJECT_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(SUBJECT_KEY)));
				break;
			case BODY_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), message.getSnippet()));
				break;
			case CC_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(FROM_KEY)));
				break;
			/*case DATE_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(DATE_KEY)));
				break;*/
			case FROM_NAME_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(FROM_NAME_KEY)));
				break;
			case TO_NAME_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(TO_NAME_KEY)));
				break;
			default:
			}
		}
		return injectedIngredients;
	}
	
	/**
	 * Si connette alle API gmail e prende tra le nuove email ricevute
	 * la prossima email non schedulata
	 * @return un oggetto che rappresenta la prossima nuova email da schedulare
	 * @throws IOException 
	 * @throws GeneralSecurityException 
	 */
	private Message getNextEmail() throws UnauthorizedChannelException, GeneralSecurityException, IOException {
		
		// Get gmail API for user
		Gmail gmail = gmailCreator.getGmail(user.getUsername());
		
		String query = createQueryString();
		
		// get last emails
		ListMessagesResponse listResponse = gmail.users().messages().list("me").setQ(query).execute();
        List<Message> messages = listResponse.getMessages();
        if (messages == null)
        	return null;
        Collections.reverse(messages);
		
        for (Message message : messages) {
        	
        	// questo messaggio è solo un header che torna gmail per efficienza, devo prendere quello completo
        	message = gmail.users().messages().get("me", message.getId()).setFormat("raw").execute();
        	
        	try {
				MimeMessage mimeMessage = getMimeMessageFromMessage(message);
				message = fillMessageFieldsFromMimeMessage(message, mimeMessage, gmail.users().getProfile("me").execute());
			} catch (MessagingException e) { continue; }
        	
        	if (new Date(message.getInternalDate()).after(getLastRefresh())) {
        		
        		System.out.println("mail id: " + message.getId());
        		System.out.println("Is new mail");
        		setLastRefresh(new Date(message.getInternalDate()));
        		if (mailSatisfyTrigger(message)) {
        			System.out.println("Mail satisfy trigger");
					return message;
        		}
        	}
        }
        return null;
        
	}
	
	private boolean mailSatisfyTrigger(Message message) {
		
		if(userIngredients.containsKey(FROM_KEY) && !userIngredients.get(FROM_KEY).equals(message.get(FROM_KEY)))
			return false;
		if(userIngredients.containsKey(TO_KEY) && !userIngredients.get(TO_KEY).equals(message.get(TO_KEY)))
			return false;
		if(userIngredients.containsKey(CC_KEY) && message.get(CC_KEY)!=null && !Arrays.asList((String[]) message.get(CC_KEY)).containsAll(Arrays.asList((String[]) userIngredients.get(CC_KEY).split(" "))))
			return false;
		if(userIngredients.containsKey(SUBJECT_KEY) && !userIngredients.get(SUBJECT_KEY).equals(message.get(SUBJECT_KEY)))
			return false;
		if(userIngredients.containsKey(BODY_KEY) && !(message.getSnippet().contains(userIngredients.get(BODY_KEY))))
			return false;
		if(userIngredients.containsKey(FROM_KEY) && !userIngredients.get(FROM_KEY).equals(message.get(FROM_KEY)))
			return false;
		return true;
	}
	
	/**
	 * crea una stringa query per cercare solo le email a partire 
	 * dalla data lastCheck e che matcha l'ingrediente from (se presente)
	 * 
	 * @return
	 */
	private String createQueryString() {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String query = "";
		if(userIngredients.containsKey(FROM_KEY))
			query += "from:" + userIngredients.get(FROM_KEY);
		query += " after:" + dateFormat.format(getLastRefresh());
		System.out.println("Query: " + query);
		return query;
	}
	
	private MimeMessage getMimeMessageFromMessage(Message message) throws MessagingException {
		
		byte[] emailBytes = Base64.decodeBase64(message.getRaw());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
        
        return email;
	}
	
	/**
	 * Riempie message con vari campi presi dal mime decodificato:
	 * - from
	 * - fromName
	 * - to
	 * - toName
	 * - cc
	 * - subject
	 * @param message
	 * @param mimeMessage
	 * @param profile
	 * @return
	 * @throws MessagingException
	 */
	private Message fillMessageFieldsFromMimeMessage(Message message, MimeMessage mimeMessage, Profile profile) throws MessagingException {
		
		if (mimeMessage.getHeader("To")[0].contains("<")) {
		message.put(FROM_KEY, StringUtils.substringBetween(mimeMessage.getHeader("From")[0], "<", ">"));
        message.put(FROM_NAME_KEY, StringUtils.substringBefore(mimeMessage.getHeader("From")[0], " <"));
		} else {
			message.put(FROM_KEY, mimeMessage.getHeader("From")[0]);
		}
        if (mimeMessage.getHeader("To") != null) {
        	if (mimeMessage.getHeader("To")[0].contains("<")) {
	        	message.put(TO_KEY, StringUtils.substringBetween(mimeMessage.getHeader("To")[0], "<", ">"));
	        	message.put(TO_NAME_KEY, StringUtils.substringBefore(mimeMessage.getHeader("To")[0], " <"));
        	} else {
        		message.put(TO_KEY, mimeMessage.getHeader("To")[0]);
        	}
        } else {
        	message.put(TO_KEY, profile.getEmailAddress());
        }
        if (mimeMessage.getHeader("Cc") != null)
        	message.put(CC_KEY, StringUtils.substringsBetween(mimeMessage.getHeader("Cc")[0], "<", ">"));
        message.put(SUBJECT_KEY, mimeMessage.getHeader("Subject")[0]);
		return message;
	}
	
}
