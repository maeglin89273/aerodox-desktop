/**
 * 
 */
package io.aerodox.desktop.service;

import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Plane2D;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

import java.awt.Dimension;
import java.awt.Toolkit;


/**
 * @author maeglin89273
 *
 */
public class Configuration {
	
	private volatile int sensitivity;
	
	private volatile Plane2D screenPlane;
	private volatile double moveThresholdSquare;
	
	private volatile double[][] pointerOffsetRot;
	
	private volatile String hostname = "Aerodox Host";
	
	private static final int SENSITIVITY_DEFAULT = 3;
	private static final int SENSITIVITY_RANGE = 10;
	
	private static final Vector2D SCREEN_SIZE;
	static {
		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 SCREEN_SIZE = new Vector2D(screenSize.getWidth(), screenSize.getHeight());
	}
	
	private static final int MIN_DISTANCE = (int)((getScreenWidth() + getScreenHeight()) / 4);
	private static final int DISTANCE_PER_SENSITIVITY = (int)(getScreenWidth() / 4.5f);
	
	private static final double UPPER_BOUND_OF_MOVE_THRESHOLD = 3;
	private static final double MOVE_THRESHOLD_PER_SENSITIVITY = -UPPER_BOUND_OF_MOVE_THRESHOLD / SENSITIVITY_RANGE;
	
	
	Configuration() {
		
		this.screenPlane = new Plane2D(new Vector3D(-getScreenWidth() / 2, 0, getScreenHeight() / 2),
								 new Vector3D(1, 0, 0), new Vector3D(0, 0, -1));
		
		this.setSensitivity(SENSITIVITY_DEFAULT);
	}
	
	public int getSensitivity() {
		return this.sensitivity;
	}
	
	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
		this.setOtherConfigsDependsOnSensitivity(sensitivity);
	}
	
	private void setOtherConfigsDependsOnSensitivity(int sensitivity) {
		double moveThreshold = sensitivity * MOVE_THRESHOLD_PER_SENSITIVITY + UPPER_BOUND_OF_MOVE_THRESHOLD;
		this.moveThresholdSquare = moveThreshold * moveThreshold;
		this.screenPlane.getOrigin().setY(MIN_DISTANCE + sensitivity * DISTANCE_PER_SENSITIVITY);
	}
	
	public double getMoveThresholdSquare() {
		return this.moveThresholdSquare;
	}
	

	public Plane2D getScreenPlane() {
		return this.screenPlane;
	}
	
	public static double getScreenWidth() {
		return SCREEN_SIZE.getX();
	}
	
	public static double getScreenHeight() {
		return SCREEN_SIZE.getY();
	}
	
	public static Vector2D getScreenSize() {
		return SCREEN_SIZE;
	}

	public double[][] getPointerOffsetRotation() {
		return this.pointerOffsetRot;
	}
	
	public void setPointerInitRotation(double[][] rotMat) {
		this.pointerOffsetRot = MathUtility.transposeMatrix(rotMat); 
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getHostname() {
		return this.hostname;
	}
}
