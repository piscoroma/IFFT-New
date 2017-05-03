package it.ifttt.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Trigger;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.repository.ActionRepository;
import it.ifttt.repository.ChannelRepository;
import it.ifttt.repository.TriggerRepository;

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
		
		/*try{
			channels = channelRepo.findAll();
			triggers = triggerRepo.findAll();
			actions = actionRepo.findAll();
		}catch(Exception e){
			log.debug("Initializing channelService...Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}*/
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
	public Channel getChannelByName(String channelName) throws DatabaseException {
		try{
			return channelRepo.findByName(channelName);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
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
	public Trigger getTriggerByName(String triggerName) throws DatabaseException {
		try{
			return triggerRepo.findByName(triggerName);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
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
	public Action getActionlByName(String actionName) throws DatabaseException {
		try{
			return actionRepo.findByName(actionName);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
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


}
