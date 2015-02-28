/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.connection.ServerConnection;

/**
 * @author maeglin89273
 *
 */
public interface HasAddressConnection extends ServerConnection {
	public abstract String getAddress();
}
