/**
 * 
 */
package io.aerodox.desktop;

import io.aerodox.desktop.connection.bluettooth.BluetoothConnection;
import io.aerodox.desktop.connection.lan.LANConnection;
import io.aerodox.desktop.gui.StatusWindow;
import io.aerodox.desktop.service.ServiceManager;

/**
 * @author maeglin89273
 *
 */
public class DesktopLunacher {
	
	public static void main(String[] args) {
		new StatusWindow().start();
//		new BluetoothConnection();
	}
	
}
