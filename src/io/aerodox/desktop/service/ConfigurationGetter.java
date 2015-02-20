/**
 * 
 */
package io.aerodox.desktop.service;

import io.aerodox.desktop.math.Plane2D;

/**
 * @author maeglin89273
 *
 */
public class ConfigurationGetter {
	
	private final Configuration wrapped;
	/**
	 * 
	 */
	ConfigurationGetter(Configuration wrapped) {
		this.wrapped = wrapped;
	}
	
	public int getSensitivity() {
		return this.wrapped.getSensitivity();
	}
	
	public double[][] getPointerOffsetMatrix() {
		return this.wrapped.getPointerOffsetRotation();
	}
	
	public Plane2D getScreenPlane() {
		return this.wrapped.getScreenPlane();
	}
	
	public double getMoveThreshold() {
		return this.wrapped.getMoveThresholdSquare();
	}
	
	public String getHostname() {
		return this.wrapped.getHostname();
	}
}
