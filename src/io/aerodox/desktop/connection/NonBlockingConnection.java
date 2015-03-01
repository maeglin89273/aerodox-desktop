/**
 * 
 */
package io.aerodox.desktop.connection;

/**
 * @author maeglin89273
 *
 */
public abstract class NonBlockingConnection implements ServerConnection {
	private Thread reciever;
	private volatile boolean closed;
	
	protected NonBlockingConnection() {
		this.closed = false;
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
	
	@Override 
	public void close() {
		if (this.isClosed()) {
			return;
		}
		this.closeImpl();
		this.closed = true;
	}
	
	@Override
	public boolean isClosed() {
		return this.closed;
	}
	protected abstract void startRecieve();
	protected abstract void closeImpl(); 
}
