/**
 * 
 */
package io.aerodox.desktop;

import io.aerodox.desktop.connection.TCPLANConnection;
import io.aerodox.desktop.connection.UDPLANConnection;
import io.aerodox.desktop.prototype.DesktopPrototype;

/**
 * @author maeglin89273
 *
 */
public class DesktopLunacher {
	public static void main(String[] args) {
		new UDPLANConnection().start();
	}
	
	
}
