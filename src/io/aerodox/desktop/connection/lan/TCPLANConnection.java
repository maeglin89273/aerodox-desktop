/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.NonBlockingConnection;
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
public class TCPLANConnection extends NonBlockingConnection {
	
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
			for (;!this.isClosed();) {
				this.lastHandler = new TCPConnectionHandler(this.delegate.accept());
				this.lastHandler.start();
//				this.executor.execute(this.lastHandler);
			}
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("lan service is closed");
		} finally {
			this.close();
		}
	}
	
	@Override
	protected void closeImpl() {
		try {
			if (this.delegate != null) {
				this.delegate.close();
				this.delegate = null;
			}
			
			this.executor.shutdown();
			this.executor = null;
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
