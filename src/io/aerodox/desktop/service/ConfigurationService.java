/**
 * 
 */
package io.aerodox.desktop.service;


/**
 * @author maeglin89273
 *
 */
public class ConfigurationService {
	
	
	private volatile int sensitivity;
	
	private static final int SENSITIVITY_DEFAULT = 5;
	private ConfigurationService() {
		System.out.println("referenced");
		this.sensitivity = SENSITIVITY_DEFAULT;
	}
	
	public int getSensitivity() {
		return this.sensitivity;
	}
	
	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	
	public static ConfigurationService getInstance() {
		return SinglotenHolder.INSTANCE;
	}
	
	//The nested class solution when multi-threading
	private static class SinglotenHolder {
		private static ConfigurationService INSTANCE = new ConfigurationService();
	}
}
