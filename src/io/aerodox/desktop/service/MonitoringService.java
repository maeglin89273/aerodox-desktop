/**
 * 
 */
package io.aerodox.desktop.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maeglin89273
 *
 */
public class MonitoringService {
	private final List<StatusListener> listenerList;
	
	private MonitoringService() {
		this.listenerList = new ArrayList<StatusListener>();
	}
	
	public static MonitoringService getInstance() {
		return SinglotenHolder.INSTANCE;
	}
	
	public synchronized void addStatusListener(StatusListener listener) {
		this.listenerList.add(listener);
	}
	
	public synchronized void removeStatusListener(StatusListener listener) {
		this.listenerList.remove(listener);
	}
	
	public void update(String statusName, Object newValue) {
		this.updateStatus(new StatusUpdateEvent(statusName, newValue));
	}
	
	private synchronized void updateStatus(StatusUpdateEvent event) {
		for (int i = this.listenerList.size(); i >= 0; i--) {
			this.listenerList.get(i).statusUpdate(event);
		}
	}
	
	private static class SinglotenHolder {
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
