/**
 * 
 */
package io.aerodox.desktop.service;

/**
 * @author maeglin89273
 *
 */
public abstract class ServiceManager {
	public static PerformingService performing() {
		return PfmSingleton.INSTANCE;
	}
	
	public static ConnectionService connection() {
		return ConSingleton.INSTANCE;
	}
	
	public static MessagingService message() {
		return MsgSingleton.INSTANCE;
		
	}
	
	public static void closeAllService() {
		MsgSingleton.INSTANCE.closeService();
		PfmSingleton.INSTANCE.closeService();
		ConSingleton.INSTANCE.closeService();
	}
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				closeAllService();
			}
		});
	}
	
	private static class MsgSingleton {
		private static final MessagingService INSTANCE = new MessagingService();
	}
	private static class PfmSingleton {
		private static final PerformingService INSTANCE = new PerformingService();
	}
	private static class ConSingleton {
		private static final ConnectionService INSTANCE = new ConnectionService();
	}
		
	
}
