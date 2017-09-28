package it.ifttt.services;

import java.util.List;

import org.bson.types.ObjectId;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Trigger;
import it.ifttt.exceptions.ActionNotFoundException;
import it.ifttt.exceptions.ChannelNotFoundException;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.exceptions.TriggerNotFoundException;

public interface ChannelService {
	
	void addChannel(Channel channel) throws DatabaseException;
	List<Channel> getAllChannels();
	Channel getChannelById(ObjectId channel_id) throws DatabaseException, ChannelNotFoundException;
	Channel getChannelByName(String channel_name) throws DatabaseException, ChannelNotFoundException;
	
	void addTrigger(Trigger trigger) throws DatabaseException;
	List<Trigger> getAllTriggers();
	Trigger getTriggerById(ObjectId trigger_id) throws DatabaseException, TriggerNotFoundException;
	Trigger getTriggerByName(String trigger_name) throws DatabaseException, TriggerNotFoundException;
	List<Trigger> getTriggersByChannel(Channel channel) throws DatabaseException;
		
	void addAction(Action action) throws DatabaseException;
	List<Action> getAllActions();
	Action getActionById(ObjectId action_id) throws DatabaseException, ActionNotFoundException;
	Action getActionByName(String action_name) throws DatabaseException, ActionNotFoundException;
	List<Action> getActionsByChannel(Channel channel) throws DatabaseException;
	
	void deleteAllCollectionChannel() throws DatabaseException;
	void addCollectionChannel(Channel channel, List<Trigger> triggers, List<Action> actions) throws DatabaseException;

}
