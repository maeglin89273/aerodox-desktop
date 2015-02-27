/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.BasicConnection;
import io.aerodox.desktop.connection.ServerConnection;
import io.aerodox.desktop.service.PerformingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author maeglin89273
 *
 */
public class TCPLANConnection extends BasicConnection {
	
	private ServerSocket delegate;
	private ExecutorService executor;
	
	private TCPConnectionHandler lastHandler;
	
	public TCPLANConnection() {
		this.executor = Executors.newCachedThreadPool();
			
		try {
			this.delegate = new ServerSocket(AerodoxConfig.TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}

	@Override
	protected void startRecieve() {
		try {
			for (;!this.delegate.isClosed();) {
				this.lastHandler = new TCPConnectionHandler(this.delegate.accept());
				this.executor.execute(this.lastHandler);
			}
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("connecting error, give it another try");
		} finally {
			this.close();
		}
	}
	
	@Override
	public void close() {
		try {
			if (this.delegate != null) {
				this.delegate.close();
			}
			
			this.executor.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public AsyncResponseChannel getResponseChannel() {
		if (this.lastHandler == null) {
			return null;
		}
		return this.lastHandler.getResponseChannel();
	}


}
