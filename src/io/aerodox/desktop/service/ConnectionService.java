/**
 * 
 */
package io.aerodox.desktop.service;

import java.util.HashSet;
import java.util.Set;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.ConnectionInfo;
import io.aerodox.desktop.connection.lan.LANConnection;
import io.aerodox.desktop.service.MonitoringService.StatusListener;
import io.aerodox.desktop.service.MonitoringService.StatusUpdateEvent;

/**
 * @author maeglin89273
 *
 */
public class ConnectionService implements StatusListener, Service {
	private LANConnection lan;
	
	private Set<ConnectionInfo> activeInfos;
	private AsyncResponseChannel rspChannel;
	
	ConnectionService() {
		setupLANConnection();
		setupBluetoothConnection();
		this.activeInfos = new HashSet<ConnectionInfo>();
		ServiceManager.monitoring().addStatusListener(this, "lan", "bluetooth");
	}

	private void setupLANConnection() {
		this.lan = new LANConnection();
		
		this.lan.start();
	}
	
	private void setupBluetoothConnection() {
		
	}
	
	@Override
	public void closeService() {
		this.lan.close();
	}
	
	public String getIP() {
		return lan.getIP();
	}
	
	public boolean isConnected() {
		return !this.activeInfos.isEmpty();
	}
	
	public AsyncResponseChannel getActiveResponseChannel() {
		return this.rspChannel;
	}

	@Override
	public void statusUpdate(StatusUpdateEvent event) {
		ConnectionInfo info = (ConnectionInfo) event.getNewValue();
		if (!info.isConnected()) {
			this.activeInfos.remove(info);
		} else {
			this.activeInfos.add(info);
		}
		updateConncetionStatus();
	}
	
	private void updateConncetionStatus() {
		String connectionStatus;
		if (this.isConnected()) {
			ConnectionInfo info = randomChooseActiveInfo();
			this.rspChannel = info.getSource().getResponseChannel();
			connectionStatus = info.toString();
		} else {
			this.rspChannel = null;
			connectionStatus = "Disconnected";
		}
		 
		ServiceManager.monitoring().update("connection", connectionStatus);
	}
	
	private ConnectionInfo randomChooseActiveInfo() {
		for (ConnectionInfo info: this.activeInfos) {
			return info;
		}
	
		return null;
	}
}
