/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.connection.Connection;
import io.aerodox.desktop.service.MonitoringService;

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
	private String ip;
	
	public LANConnection() {
		this.tcp = new TCPLANConnection();
		this.udp = new UDPLANConnection();
	}

	@Override
	public void start() {
		this.setupIP();
		this.udp.start();
		this.tcp.start();
	}

	@Override
	public void close() {
		this.udp.close();
		this.tcp.close();
	}
	
	private void setupIP() {
		List<String> ips = getActiveIPs();
		if (ips.isEmpty()) {
			return;
		}
		
		// should perform active ip test
		this.ip = ips.get(0);
		
		
	}
	
	public String getIP() {
		if (this.ip == null) {
			this.setupIP();
		}
		
		return this.ip;
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
