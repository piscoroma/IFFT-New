package it.ifttt.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Trigger;
import it.ifttt.exceptions.ActionNotFoundException;
import it.ifttt.exceptions.ChannelNotFoundException;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.exceptions.TriggerNotFoundException;
import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.messages.ChannelStatusMessage;
import it.ifttt.repository.ActionRepository;
import it.ifttt.repository.ChannelRepository;
import it.ifttt.repository.TriggerRepository;
import it.ifttt.social_api_creators.GcalendarCreator;
import it.ifttt.social_api_creators.GmailCreator;
import it.ifttt.social_api_creators.TwitterTemplateCreator;
import it.ifttt.springSocialMongo.ConnectionConverter;
import it.ifttt.springSocialMongo.MongoConnection;
import it.ifttt.springSocialMongo.MongoConnectionService;

@Service
public class ChannelServiceImpl implements ChannelService {

	private final static Logger log = Logger.getLogger(ChannelServiceImpl.class);

	@Autowired
	private ChannelRepository channelRepo;
	@Autowired
	private TriggerRepository triggerRepo;
	@Autowired
	private ActionRepository actionRepo;
	
	List<Channel> channels;
	List<Trigger> triggers;
	List<Action> actions;
		
	@PostConstruct
	private void init() throws RuntimeException{
		log.debug("Initializing channelService...");
		channels = Collections.synchronizedList(new ArrayList<Channel>());
		triggers = Collections.synchronizedList(new ArrayList<Trigger>());
		actions = Collections.synchronizedList(new ArrayList<Action>());
		
		try{
			channels = channelRepo.findAll();
			triggers = triggerRepo.findAll();
			actions = actionRepo.findAll();
		}catch(Exception e){
			log.debug("Initializing channelService...Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
		log.debug("Initializing channelService...done!");
	}
	
	@Override
	public void addChannel(Channel channel) throws DatabaseException {
		try{
			channelRepo.save(channel);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		channels.add(channel);
		
	}
	
	@Override
	public List<Channel> getAllChannels(){
		return channels;
	}
	
	@Override
	public Channel getChannelById(ObjectId channel_id) throws DatabaseException, ChannelNotFoundException {
		Channel channel = null;
		try{
			channel = channelRepo.findById(channel_id);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(channel == null){
			throw new ChannelNotFoundException();
		}
		return channel;
	}
	
	@Override
	public Channel getChannelByName(String channel_name) throws DatabaseException, ChannelNotFoundException {
		Channel channel = null;
		try{
			channel = channelRepo.findByName(channel_name);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(channel == null){
			throw new ChannelNotFoundException();
		}
		return channel;
	}

	@Override
	public void addTrigger(Trigger trigger) throws DatabaseException {
		try{
			triggerRepo.save(trigger);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		triggers.add(trigger);	
	}
	
	@Override
	public List<Trigger> getAllTriggers(){
		return triggers;
	}
	
	@Override
	public Trigger getTriggerById(ObjectId trigger_id) throws DatabaseException, TriggerNotFoundException {
		Trigger trigger = null;
		try{
			trigger = triggerRepo.findById(trigger_id);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(trigger == null){
			throw new TriggerNotFoundException();
		}
		return trigger;
	}
	
	@Override
	public Trigger getTriggerByName(String trigger_name) throws DatabaseException, TriggerNotFoundException {
		Trigger trigger = null;
		try{
			trigger = triggerRepo.findByName(trigger_name);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(trigger == null){
			throw new TriggerNotFoundException();
		}
		return trigger;
	}
	
	@Override
	public List<Trigger> getTriggersByChannel(Channel channel) throws DatabaseException {
		try{
			return triggerRepo.findByChannel(channel);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}

	@Override
	public void addAction(Action action) throws DatabaseException {
		try{
			actionRepo.save(action);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		actions.add(action);
	}
	
	@Override
	public List<Action> getAllActions() {
		return actions;
	}
	
	@Override
	public Action getActionById(ObjectId action_id) throws DatabaseException, ActionNotFoundException {
		Action action = null;
		try{
			action = actionRepo.findById(action_id);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(action == null){
			throw new ActionNotFoundException();
		}
		return action;
	}
	
	@Override
	public Action getActionByName(String action_name) throws DatabaseException, ActionNotFoundException {
		Action action = null;
		try{
			action = actionRepo.findByName(action_name);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(action == null){
			throw new ActionNotFoundException();
		}
		return action;
	}

	@Override
	public List<Action> getActionsByChannel(Channel channel) throws DatabaseException {
		try{
			return actionRepo.findByChannel(channel);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}

	@Override
	public void addCollectionChannel(Channel channel, List<Trigger> triggers, List<Action> actions) throws DatabaseException {
		try{
			channelRepo.save(channel);
			for(Trigger trigger : triggers)
				triggerRepo.save(trigger);
			for(Action action : actions)
				actionRepo.save(action);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		this.channels.add(channel);
		for(Trigger trigger : triggers)
			this.triggers.add(trigger);
		for(Action action : actions)
			this.actions.add(action);
	}
	
	@Override
	public void deleteAllCollectionChannel() throws DatabaseException {
		try{
			channelRepo.deleteAll();
			triggerRepo.deleteAll();
			actionRepo.deleteAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		channels.clear();
		triggers.clear();
		actions.clear();
	}
	
	private enum ProviderId {
	    TWITTER, GOOGLE;
	}

	@Autowired
	private MongoConnectionService mongoConnectionService;
	@Autowired
	private ConnectionConverter connectionConverter;
	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;
	@Autowired
	private GmailCreator gmailCreator;
	@Autowired
	private GcalendarCreator calendarCreator;

	@Override
	public List<ChannelStatusMessage> getAllStatus() {
		List<ChannelStatusMessage> statusList = new ArrayList<>();
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
    	for(ProviderId provider: ProviderId.values())
    	{
    		MongoConnection channelInfo = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(userId, provider.name().toLowerCase()));
        	ChannelStatusMessage channelStatusMessage = new ChannelStatusMessage();
        	channelStatusMessage.setChannel(provider.name().toLowerCase());
        	if((channelInfo)==null || !checkAuthorizedChannel(userId, provider.name().toLowerCase()))
        		channelStatusMessage.setLogged(false);
        	else
        		channelStatusMessage.setLogged(true);
        	statusList.add(channelStatusMessage);
    	}
    	return statusList;
	}
	
	public ChannelStatusMessage getStatus(String providerId) {
    	String userId = SecurityContextHolder.getContext().getAuthentication().getName();
    	MongoConnection channelInfo = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(userId, providerId));
    	ChannelStatusMessage channelStatusMessage = new ChannelStatusMessage();
    	channelStatusMessage.setChannel(providerId);
    	if((channelInfo)==null || !checkAuthorizedChannel(userId, providerId))
    		channelStatusMessage.setLogged(false);
    	else
    		channelStatusMessage.setLogged(true);
    	return channelStatusMessage;
    }
	
	private boolean checkAuthorizedChannel(String userId, String providerId) {
		
		try {
			switch (providerId) {
				case GmailCreator.GOOGLE_ID:
					gmailCreator.assertAuthorizedChannel(userId);
					calendarCreator.assertAuthorizedChannel(userId);
					break;
				case TwitterTemplateCreator.TWITTER_ID:
					twitterTemplateCreator.assertAuthorizedChannel(userId);
					break;
			}
			return true;
		} catch (UnauthorizedChannelException ex) {
			return false;
		}
	}

}
