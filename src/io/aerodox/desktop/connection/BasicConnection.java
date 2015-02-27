/**
 * 
 */
package io.aerodox.desktop.connection;

/**
 * @author maeglin89273
 *
 */
public abstract class BasicConnection implements ServerConnection {
	private Thread reciever;
	
	protected BasicConnection() {
		this.reciever = new Thread() {
			@Override
			public void run() {
				startRecieve();
			}
		};
	}
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.Connection#start()
	 */
	@Override
	public void start() {
		reciever.start();
	}
	
	protected abstract void startRecieve();
}
