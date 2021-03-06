/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.connection.Connection;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author maeglin89273
 *
 */
public class LANConnection implements Connection {
	
	private Connection tcp;
	private Connection udp;
	private Thread subConnectionThread;
	
	public LANConnection() {
		this.tcp = new TCPLANConnection();
		this.udp = new UDPLANConnection();
		this.subConnectionThread = new Thread(new Runnable() {

			@Override
			public void run() {
				udp.start();
			}
			
		});
	}

	@Override
	public void start() {
		this.showIPs();
		this.subConnectionThread.start();
		this.tcp.start();
	}

	@Override
	public void close() {
		this.udp.close();
		this.tcp.close();
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


}
