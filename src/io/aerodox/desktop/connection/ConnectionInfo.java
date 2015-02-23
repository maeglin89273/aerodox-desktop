/**
 * 
 */
package io.aerodox.desktop.connection;

/**
 * @author maeglin89273
 *
 */
public class ConnectionInfo {
	public enum ConnectionType {LAN, BLUETOOTH};
	private boolean connected;
	private final ConnectionType type;
	
	public ConnectionInfo(boolean connected, ConnectionType type) {
		this.setConnected(connected);
		this.type = type;
			
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public ConnectionType getType() {
		return type;
	}

	
}
