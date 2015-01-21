/**
 * 
 */
package io.aerodox.desktop.service;

import io.aerodox.desktop.math.Plane2D;
import io.aerodox.desktop.math.Vector3D;

import java.awt.Dimension;
import java.awt.Toolkit;


/**
 * @author maeglin89273
 *
 */
public class ConfigurationService {
	
	
	private volatile int sensitivity;
	private volatile Plane2D screenPlane;
	private static final int DISTANCE_FACTOR = 950;
	private static final float PROJ_SHRINK_FACTOR = 0.75f;
	private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final int SENSITIVITY_DEFAULT = 1;
	private ConfigurationService() {
		this.sensitivity = SENSITIVITY_DEFAULT;
		this.screenPlane = new Plane2D(new Vector3D((float)-SCREEN_SIZE.getWidth() / 2, this.getDistance(), (float)SCREEN_SIZE.getHeight() / 2),
								 new Vector3D(1, 0, 0), new Vector3D(0, 0, -1));
	}
	
	public int getSensitivity() {
		return this.sensitivity;
	}
	
	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
		this.screenPlane.getOrigin().setY(this.getDistance());
	}
	
	public float getDistance() {
		return this.getSensitivity() * DISTANCE_FACTOR;
	}
	public float getProjectScale() {
		return PROJ_SHRINK_FACTOR * getSensitivity();
	}
	
	public float getScreenWidth() {
		return (float)this.SCREEN_SIZE.getWidth();
	}
	
	public float getScreenHeight() {
		return (float)this.SCREEN_SIZE.getHeight();
	}
	
	
	public Plane2D getScreenPlane() {
		return this.screenPlane;
	}
	
	public static ConfigurationService getInstance() {
		return SinglotenHolder.INSTANCE;
	}
	
	
	//The nested class solution when multi-threading
	private static class SinglotenHolder {
		private static ConfigurationService INSTANCE = new ConfigurationService();
	}
}
