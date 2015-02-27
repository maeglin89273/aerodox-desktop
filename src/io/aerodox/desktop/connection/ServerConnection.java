/**
 * 
 */
package io.aerodox.desktop.connection;

/**
 * @author maeglin89273
 *
 */
public interface ServerConnection {
	
	public abstract void start();
	public abstract AsyncResponseChannel getResponseChannel();
	public abstract void close();
}
