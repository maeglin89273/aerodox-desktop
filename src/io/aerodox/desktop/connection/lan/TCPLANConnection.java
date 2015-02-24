/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.connection.Connection;
import io.aerodox.desktop.service.PerformingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author maeglin89273
 *
 */
public class TCPLANConnection implements Connection {
	
	private ServerSocket delegate;
	private ExecutorService executor;
	
	
	public TCPLANConnection() {
		this.executor = Executors.newCachedThreadPool();
			
		try {
			this.delegate = new ServerSocket(AerodoxConfig.TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.Connection#start()
	 */
	@Override
	public void start() {
		listenRequests();
	}
	
	private void listenRequests() {
		try {
			for (;!this.delegate.isClosed();) {
				this.executor.execute(new TCPConnectionHandler(this.delegate.accept()));
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			PerformingService.getInstance().closeService();
		}
	}

}
