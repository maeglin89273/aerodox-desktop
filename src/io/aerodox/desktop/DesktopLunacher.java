/**
 * 
 */
package io.aerodox.desktop;

import io.aerodox.desktop.connection.TCPLANConnection;
import io.aerodox.desktop.connection.UDPLANConnection;

/**
 * @author maeglin89273
 *
 */
public class DesktopLunacher {
	public static void main(String[] args) {
		Thread udpThread = new Thread(new UDPThread());
		udpThread.start();
		
		new TCPLANConnection().start();
	}
	
	private static class UDPThread implements Runnable {

		@Override
		public void run() {
			new UDPLANConnection().start();
		}
		
	}
	
}
