/**
 * 
 */
package io.aerodox.desktop.service;

import io.aerodox.desktop.math.MathUtility;
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
	private volatile int distance;
	private volatile double rotationThreshold;
	
	private static final int SENSITIVITY_DEFAULT = 3;
	private static final int SENSITIVITY_RANGE = 10;
	
	private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int MIN_DISTANCE = (int)((getScreenWidth() + getScreenHeight()) / 4);
	private static final int DISTANCE_PER_SENSITIVITY = (int)getScreenWidth() / 6;
	
	private static final double DEGREE_TO_RADIAN = Math.PI / 180;
	private static final double UPPER_BOUND_OF_MIN_ROTATION_DEGREE = 0.1;
	private static final double DEGREE_PER_SENSITIVITY = -UPPER_BOUND_OF_MIN_ROTATION_DEGREE / SENSITIVITY_RANGE;
	
	
	private ConfigurationService() {
		
		this.screenPlane = new Plane2D(new Vector3D(-getScreenWidth() / 2, this.getDistance(), getScreenHeight() / 2),
								 new Vector3D(1, 0, 0), new Vector3D(0, 0, -1));
		
		this.setSensitivity(SENSITIVITY_DEFAULT);
	}
	
	public int getSensitivity() {
		return this.sensitivity;
	}
	
	public synchronized void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
		this.setOtherConfigsDependsOnSensitivity(sensitivity);
	}
	
	private void setOtherConfigsDependsOnSensitivity(int sensitivity) {
		this.rotationThreshold = Math.sin((sensitivity * DEGREE_PER_SENSITIVITY + UPPER_BOUND_OF_MIN_ROTATION_DEGREE) * DEGREE_TO_RADIAN * 0.5);
		this.distance = MIN_DISTANCE + sensitivity * DISTANCE_PER_SENSITIVITY;
		this.screenPlane.getOrigin().setY(this.getDistance());
	}
	
	public float getDistance() {
		return this.distance;
	}
	
	public double getRotationThreshold() {
		return this.rotationThreshold;
	}
	
	public static float getScreenWidth() {
		return (float)SCREEN_SIZE.getWidth();
	}
	
	public static float getScreenHeight() {
		return (float)SCREEN_SIZE.getHeight();
	}
	
	public Plane2D getScreenPlane() {
		return this.screenPlane;
	}
	
	public static ConfigurationService getInstance() {
		return SinglotenHolder.INSTANCE;
	}
	
	//The nested class solution of Singleton when multi-threading
	private static class SinglotenHolder {
		private static ConfigurationService INSTANCE = new ConfigurationService();
	}
}
