/**
 * 
 */
package io.aerodox.desktop.connection.bluettooth;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.NonBlockingConnection;
import io.aerodox.desktop.connection.ServerConnection;
import io.aerodox.desktop.connection.lan.HasAddressConnection;
import io.aerodox.desktop.service.ServiceManager;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @author maeglin89273
 *
 */
public class BluetoothConnection extends NonBlockingConnection implements HasAddressConnection {
	private StreamConnectionNotifier delegate;
	private ServerConnection connHandler;
	private String address;
	
	public BluetoothConnection() {
		this.delegate = initBluetoothServer();
	}
	
	private StreamConnectionNotifier initBluetoothServer() {
		StreamConnectionNotifier notifier = null;
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			this.address = formatAddress(localDevice.getBluetoothAddress());
			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			
			notifier = openNewServerSocket();
		} catch (IOException e) {
//			e.printStackTrace();
			ServiceManager.message().send("info", "The bluetooth is not available.\n"
					+ "If you want to connection via bluetooth, please turn on your bluetooth and restart Aerodox");
			this.close();
		}
		return notifier;
	}
	
	private static StreamConnectionNotifier openNewServerSocket() throws IOException {
		return (StreamConnectionNotifier) Connector.open(BluetoothConfig.URL);
	}
	
	static String formatAddress(String btAddress) {
		StringBuilder sb = new StringBuilder(btAddress.length() + (btAddress.length() / 2));
		for (int i = 0; i < btAddress.length(); i += 2) {
			sb.append(btAddress.substring(i, i + 2));
			sb.append(":");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	@Override
	public String getAddress() {
		return this.address == null? "OFF": this.address;
	}
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.ServerConnection#getResponseChannel()
	 */
	@Override
	public AsyncResponseChannel getResponseChannel() {
		if (this.connHandler == null) {
			return null;
		}
		return this.connHandler.getResponseChannel();
	}

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.ServerConnection#close()
	 */
	@Override
	protected void closeImpl() {
		
		try {
			if (this.delegate != null) {
				this.delegate.close();
				this.delegate = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		operation is not permitted
//		localDevice.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE);
	}

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.BasicConnection#startRecieve()
	 */
	@Override
	protected void startRecieve() {
		if (this.delegate == null) {
			return;
		}
		
		try {
			for (;!this.isClosed();) {
				this.connHandler = new BluetoothConnectionHandler(this.delegate.acceptAndOpen());
				this.closeImpl();
				this.connHandler.start();
				
				this.delegate = openNewServerSocket();
			}
			
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("bluetooth service is closed");
		}finally {
			this.close();
		}
				
	}
}
