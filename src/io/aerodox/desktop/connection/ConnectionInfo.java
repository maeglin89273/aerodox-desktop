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
	private final ServerConnection soruce;
	private final ConnectionType type;
	private final String deviceName;
	
	public ConnectionInfo(ServerConnection source, boolean connected, ConnectionType type, String deviceName) {
		soruce = source;
		this.setConnected(connected);
		this.type = type;
		this.deviceName = deviceName;
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
	
	public String getDeviceName() {
		return deviceName;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ConnectionInfo) {
			ConnectionInfo connection = (ConnectionInfo)o;
			return this.isConnected() == connection.isConnected() &&
				   this.getType() == connection.getType() &&
				   this.getDeviceName().equals(connection.getDeviceName());
		}
		
		return false;
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder(this.isConnected()? "Connected" : "Disconnected");
		strBuilder.append(" (");
		strBuilder.append(this.getType().name().toLowerCase());
		strBuilder.append(" from ");
		strBuilder.append(this.getDeviceName());
		strBuilder.append(")");
		return strBuilder.toString();
	}

	public ServerConnection getSource() {
		return soruce;
	}
	
}
