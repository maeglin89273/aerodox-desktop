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
public class MonitoringService {
	private final Map<String, List<StatusListener>> listenerMap;
	
	private MonitoringService() {
		this.listenerMap = new HashMap<String, List<StatusListener>>();
	}
	
	public static MonitoringService getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public synchronized void addStatusListener(StatusListener listener, String... interestedStatuses) {
		List<StatusListener> listeners;
		for (String status: interestedStatuses) {
			listeners = getListenerList(status);
			listeners.add(listener);
		}
		
	}
	
	private List<StatusListener> getListenerList(String status) {
		List<StatusListener> listeners = this.listenerMap.get(status);
		if (listeners == null) {
			listeners = new ArrayList<StatusListener>();
			this.listenerMap.put(status, listeners);
		}
		
		return listeners;
	}

	public synchronized void removeStatusListener(StatusListener listener) {
		for (Entry<String, List<StatusListener>> pair: this.listenerMap.entrySet()) {
			pair.getValue().remove(listener);
		}
	}
	
	public void update(String statusName, Object newValue) {
		this.updateStatus(new StatusUpdateEvent(statusName, newValue));
	}
	
	private synchronized void updateStatus(StatusUpdateEvent event) {
		List<StatusListener> listeners = this.listenerMap.get(event.getStatusName());
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).statusUpdate(event);
		}
	}
	
	private static class SingletonHolder {
		private static final MonitoringService INSTANCE = new MonitoringService();
	}
	
	public interface StatusListener {
		public abstract void statusUpdate(StatusUpdateEvent event);
	}
	
	public class StatusUpdateEvent {
		private final String statusName;
		private final Object newValue;
		
		private StatusUpdateEvent(String statusName, Object newValue) {
			this.statusName = statusName;
			this.newValue = newValue;
		}
		
		public String getStatusName() {
			return this.statusName;
		}
		
		public Object getNewValue() {
			return this.newValue;
		}
	}
}
