package it.ifttt.services;

import java.util.List;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Trigger;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.messages.ChannelStatusMessage;

public interface ChannelService {
	
	void addChannel(Channel channel) throws DatabaseException;
	List<Channel> getAllChannels();
	Channel getChannelByName(String channelName) throws DatabaseException;
	
	void addTrigger(Trigger trigger) throws DatabaseException;
	List<Trigger> getAllTriggers();
	Trigger getTriggerByName(String triggerName) throws DatabaseException;
	List<Trigger> getTriggersByChannel(Channel channel) throws DatabaseException;
		
	void addAction(Action action) throws DatabaseException;
	List<Action> getAllActions();
	Action getActionlByName(String actionName) throws DatabaseException;
	List<Action> getActionsByChannel(Channel channel) throws DatabaseException;
	
	void deleteAllCollectionChannel() throws DatabaseException;
	void addCollectionChannel(Channel channel, List<Trigger> triggers, List<Action> actions) throws DatabaseException;

}
