/**
 * 
 */
package io.aerodox.desktop.imitation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.translation.Action;

/**
 * @author maeglin89273
 *
 */
public class PerformingService {
	private static PerformingService instance = null;
	private Performer performer;
	private ExecutorService executor;
	private PerformingService() {
		this.performer = new Performer();
		this.executor = Executors.newSingleThreadExecutor();
	}
	
	public static PerformingService getInstance() {
		if (instance == null) {
			instance = new PerformingService();
		}
		
		return instance;
	}
	
	public void queueAction(final Action action, final AsyncResponseChannel channel) {
		this.executor.execute(new Runnable() {

			@Override
			public void run() {
				Object response = action.perform(performer);
				if (response != null) {
					channel.respond(response);
				}
			}
			
		});
	}
	
	public void closeService() {
		this.executor.shutdownNow();
	}
}
