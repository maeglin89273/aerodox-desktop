/**
 * 
 */
package io.aerodox.desktop.connection;

import java.util.HashSet;
import java.util.Set;

import io.aerodox.desktop.connection.lan.LANConnection;
import io.aerodox.desktop.service.MonitoringService;
import io.aerodox.desktop.service.MonitoringService.StatusListener;
import io.aerodox.desktop.service.MonitoringService.StatusUpdateEvent;

/**
 * @author maeglin89273
 *
 */
public class ConnectionManager implements StatusListener {
	private LANConnection lan;
	private Thread lanThread;
	
	private Set<ConnectionInfo> activeInfos;
	private ConnectionManager() {
		setupLANConnection();
		setupBluetoothConnection();
		this.activeInfos = new HashSet<ConnectionInfo>();
		MonitoringService.getInstance().addStatusListener(this, "lan", "bluetooth");
	}
	
	public static ConnectionManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private void setupLANConnection() {
		this.lan = new LANConnection();
		this.lanThread = new Thread() {
			@Override
			public void run() {
				lan.start();
			}
		};
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				lan.close();
				try {
					lanThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		this.lanThread.start();
	}
	
	private void setupBluetoothConnection() {
		
	}
	
	public String getIP() {
		return lan.getIP();
	}
	
	public boolean isConnected() {
		return !this.activeInfos.isEmpty();
	}
	private static class SingletonHolder {
		private static final ConnectionManager INSTANCE = new ConnectionManager();
	}

	@Override
	public void statusUpdate(StatusUpdateEvent event) {
		ConnectionInfo info = (ConnectionInfo) event.getNewValue();
		if (!info.isConnected()) {
			this.activeInfos.remove(info);
		} else {
			this.activeInfos.add(info);
		}
		notifyInfoUpdate();
	}

	private void notifyInfoUpdate() {
		String connectionStatus = this.isConnected()? randomChooseActiveInfo().toString(): "Disconnected";
		MonitoringService.getInstance().update("connection", connectionStatus);
	}
	
	private ConnectionInfo randomChooseActiveInfo() {
		for (ConnectionInfo info: this.activeInfos) {
			return info;
		}
		return null;
	}
}
