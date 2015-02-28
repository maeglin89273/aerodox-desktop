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
		return SingletonHolder.PERFORM_INST;
	}
	
	public static ConnectionService connection() {
		return SingletonHolder.CONNECTION_INST;
	}
	
	public static MessagingService message() {
		return SingletonHolder.MONITOR_INST;
		
	}
	
	public static void closeAllService() {
		SingletonHolder.closeAllService();
	}
	
	private static class SingletonHolder {
		private static final PerformingService PERFORM_INST = new PerformingService();
		private static final MessagingService MONITOR_INST = new MessagingService();
		private static final ConnectionService CONNECTION_INST = new ConnectionService();
		
		static {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					closeAllService();
				}
			});
		}
		
		private static void closeAllService() {
			CONNECTION_INST.closeService();
			PERFORM_INST.closeService();
			MONITOR_INST.closeService();
		}
	}
}
