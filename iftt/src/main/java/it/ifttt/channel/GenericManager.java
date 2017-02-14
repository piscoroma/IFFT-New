package it.ifttt.channel;

public interface GenericManager {
	TriggerEvent setTriggerEvent(String triggerName);
	ActionPerformer setActionPerformer(String actionName);
}
