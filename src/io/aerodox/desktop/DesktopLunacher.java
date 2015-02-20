/**
 * 
 */
package io.aerodox.desktop;

import io.aerodox.desktop.connection.lan.LANConnection;
import io.aerodox.desktop.connection.lan.TCPLANConnection;
import io.aerodox.desktop.connection.lan.UDPLANConnection;

/**
 * @author maeglin89273
 *
 */
public class DesktopLunacher {
	
	public static void main(String[] args) {
		new LANConnection().start();
	}
	
}
