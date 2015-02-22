/**
 * 
 */
package io.aerodox.desktop;

import io.aerodox.desktop.connection.lan.LANConnection;

/**
 * @author maeglin89273
 *
 */
public class DesktopLunacher {
	
	public static void main(String[] args) {
		new LANConnection().start();
		
	}
	
}
