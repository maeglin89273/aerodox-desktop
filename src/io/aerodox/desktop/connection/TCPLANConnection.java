/**
 * 
 */
package io.aerodox.desktop.connection;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.service.PerformingService;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author maeglin89273
 *
 */
public class TCPLANConnection extends LANConnection {
	
	private ServerSocket delegate;
	private ExecutorService executor;
	public TCPLANConnection() {
		this.executor = Executors.newCachedThreadPool();
				
		try {
			this.delegate = new ServerSocket(AerodoxConfig.PORT);
			
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.Connection#start()
	 */
	@Override
	public void start() {
		showIPs();
		listenActions();
		
	}
	
	private void listenActions() {
		try {
			for (;!this.delegate.isClosed();) {
				this.executor.execute(new ConnectionHandler(this.delegate.accept()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("connecting error, give it another try");
		}
	}


	@Override
	public void close() {
		try {
			if (this.delegate != null) {
				this.delegate.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		PerformingService.getInstance().closeService();
	}

}
