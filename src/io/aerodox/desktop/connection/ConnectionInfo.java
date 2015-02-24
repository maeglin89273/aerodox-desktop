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
	private final String deviceAddress;
	
	public ConnectionInfo(boolean connected, ConnectionType type, String deviceAddress) {
		this.setConnected(connected);
		this.type = type;
		this.deviceAddress = deviceAddress;
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
	
	public String getDeviceAddress() {
		return deviceAddress;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ConnectionInfo) {
			ConnectionInfo connection = (ConnectionInfo)o;
			return this.isConnected() == connection.isConnected() &&
				   this.getType() == connection.getType() &&
				   this.getDeviceAddress().equals(connection.getDeviceAddress());
		}
		
		return false;
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder(this.isConnected()? "Connected" : "Disconnected");
		strBuilder.append(" (");
		strBuilder.append(this.getType().name().toLowerCase());
		strBuilder.append(" from ");
		strBuilder.append(this.getDeviceAddress());
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
}
