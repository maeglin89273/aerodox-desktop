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
public class LANConnection implements Connection {
	
	private ServerSocket delegate;
	private ExecutorService executor;
	public LANConnection() {
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


	private void showIPs() {
		List<String> ips = getActiveIPs();
		System.out.println("The host ip is:");
		for (String ip: ips) {
			System.out.println(ip);
		}
	}

	private List<String> getActiveIPs() {
		List<String> result = new LinkedList<String>();  
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface netInterface;
			for (; netInterfaces.hasMoreElements();) {
				netInterface = netInterfaces.nextElement();
				if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp()) {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					InetAddress address;
					for (; addresses.hasMoreElements();) {
						 address = addresses.nextElement();
						if (address instanceof Inet4Address) {
							result.add(address.getHostAddress());
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public void close() {
		try {
			this.delegate.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PerformingService.getInstance().closeService();
	}

}
