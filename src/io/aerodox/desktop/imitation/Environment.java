/**
 * 
 */
package io.aerodox.desktop.imitation;

import java.awt.MouseInfo;
import java.awt.Point;

import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.ConfigurationService;

/**
 * @author maeglin89273
 *
 */
public class Environment {
	private Vector2D velocity;
	private Vector3D pointerVec;
	
	public Environment() {
		this.velocity = new Vector2D();
		this.pointerVec = new Vector3D(0, 1, 0);
	}
	
	public Vector2D getVelocityReference() {
		return this.velocity;
	}
	
	public Vector2D getMousePosition() {
		return new Vector2D(MouseInfo.getPointerInfo().getLocation());
	}
	
	public Vector3D getPointerReference() {
		return this.pointerVec;
	}
	
	
}
