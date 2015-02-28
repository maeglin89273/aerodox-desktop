/**
 * 
 */
package io.aerodox.desktop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author maeglin89273
 *
 */
public class MessagingService implements Service {
	private final Map<String, List<MessageListener>> listenerMap;
	
	MessagingService() {
		this.listenerMap = new HashMap<String, List<MessageListener>>();
	}
	
	public synchronized void addMessageListener(MessageListener listener, String... interestedStatuses) {
		List<MessageListener> listeners;
		for (String status: interestedStatuses) {
			listeners = getListenerList(status);
			listeners.add(listener);
		}
		
	}
	
	private List<MessageListener> getListenerList(String status) {
		List<MessageListener> listeners = this.listenerMap.get(status);
		if (listeners == null) {
			listeners = new ArrayList<MessageListener>();
			this.listenerMap.put(status, listeners);
		}
		
		return listeners;
	}

	public synchronized void removeMessageListener(MessageListener listener) {
		for (Entry<String, List<MessageListener>> pair: this.listenerMap.entrySet()) {
			pair.getValue().remove(listener);
		}
	}
	
	public void send(String statusName, Object newValue) {
		this.sendToListeners(new Message(statusName, newValue));
	}
	
	private synchronized void sendToListeners(Message event) {
		List<MessageListener> listeners = this.listenerMap.get(event.getWhat());
		if (listeners != null) {
			for (int i = listeners.size() - 1; i >= 0; i--) {
				listeners.get(i).handleMessage(event);
			}
		}
	}
	
	public interface MessageListener {
		public abstract void handleMessage(Message message);
	}
	
	public class Message {
		private final String what;
		private final Object value;
		
		private Message(String what, Object value) {
			this.what = what;
			this.value = value;
		}
		
		public String getWhat() {
			return this.what;
		}
		
		public Object getValue() {
			return this.value;
		}
	}

	@Override
	public void closeService() {
		this.listenerMap.clear();
	}
}
