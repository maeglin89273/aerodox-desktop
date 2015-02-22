/**
 * 
 */
package io.aerodox.desktop.service;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.translation.Action;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author maeglin89273
 *
 */
public class PerformingService {
	private Configuration config;
	private ConfigurationGetter configGetter;
	private Performer performer;
	private MotionTools tools;
	private ExecutorService executor;
	
	private PerformingService() {
		this.config = new Configuration();
		this.configGetter = new ConfigurationGetter(this.config);
		this.performer = new Performer();
		this.tools = new MotionTools();
		this.executor = Executors.newSingleThreadExecutor();
	}
	
	public static PerformingService getInstance() {
		return SinglotenHolder.INSTANCE;
	}
	
	public ConfigurationGetter getConfigGetter() {
		return this.configGetter;
	}
	
	public void queueAction(final Action action, final AsyncResponseChannel channel) {
		this.executor.execute(new Runnable() {

			@Override
			public void run() {
				JsonResponse response = action.perform(performer, tools, config);
				if (response != null) {
					channel.respond(response);
				}
			}
			
		});
	}
	
	public void closeService() {
		this.executor.shutdownNow();
	}
	
	private static class SinglotenHolder {
		private static final PerformingService INSTANCE = new PerformingService();
	}
}
