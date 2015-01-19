/**
 * 
 */
package io.aerodox.desktop.imitation;

import java.awt.MouseInfo;
import java.awt.Point;

import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

/**
 * @author maeglin89273
 *
 */
public class Environment {
	private Vector3D velocity;
	public Environment() {
		this.velocity = new Vector3D();
	}
	
	public Vector3D getVelocityReference() {
		return this.velocity;
	}
	
	public Vector2D getMousePosition() {
		return new Vector2D(MouseInfo.getPointerInfo().getLocation());
	}
}
