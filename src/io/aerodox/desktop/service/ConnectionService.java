/**
 * 
 */
package io.aerodox.desktop.service;

import java.util.HashSet;
import java.util.Set;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.ConnectionInfo;
import io.aerodox.desktop.connection.bluettooth.BluetoothConnection;
import io.aerodox.desktop.connection.lan.HasAddressConnection;
import io.aerodox.desktop.connection.lan.LANConnection;
import io.aerodox.desktop.service.MessagingService.MessageListener;
import io.aerodox.desktop.service.MessagingService.Message;

/**
 * @author maeglin89273
 *
 */
public class ConnectionService implements MessageListener, Service {
	private HasAddressConnection lan;
	private HasAddressConnection bluetooth;
	
	private Set<ConnectionInfo> activeInfos;
	private AsyncResponseChannel rspChannel;
	
	ConnectionService() {
		setupLANConnection();
		setupBluetoothConnection();
		this.activeInfos = new HashSet<ConnectionInfo>();
		ServiceManager.message().addMessageListener(this, "lan", "bluetooth");
	}

	private void setupLANConnection() {
		this.lan = new LANConnection();
		this.lan.start();
	}
	
	private void setupBluetoothConnection() {
		this.bluetooth = new BluetoothConnection();
		this.bluetooth.start();
	}
	
	@Override
	public void closeService() {
		this.lan.close();
		this.bluetooth.close();
	}
	
	public String getIP() {
		return this.lan.getAddress();
	}
	
	public String getBlutoothMAC() {
		return this.bluetooth.getAddress();
	}
	
	public boolean isConnected() {
		return !this.activeInfos.isEmpty();
	}
	
	public AsyncResponseChannel getActiveResponseChannel() {
		return this.rspChannel;
	}

	@Override
	public void handleMessage(Message message) {
		ConnectionInfo info = (ConnectionInfo) message.getValue();
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
		 
		ServiceManager.message().send("connection", connectionStatus);
	}
	
	private ConnectionInfo randomChooseActiveInfo() {
		for (ConnectionInfo info: this.activeInfos) {
			return info;
		}
	
		return null;
	}
}
