package it.ifttt.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "channels")
public class CollectionChannel {

	@Id
	private ObjectId id;
	private Channel channel;
	private List<Trigger> triggers;
	private List<Action> actions;
	
	public CollectionChannel(Channel channel, List<Trigger> triggers, List<Action> actions) {
		this.channel = channel;
		this.triggers = triggers;
		this.actions = actions;
	}
	
	public ObjectId getId() {
		return id;
	}

	public Channel getChannel() {
		return channel;
	}	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public List<Trigger> getTriggers() {
		return triggers;
	}
	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	
	
}
