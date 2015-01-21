/**
 * 
 */
package io.aerodox.desktop.math;

import java.awt.Point;

/**
 * @author maeglin89273
 *
 */
public class Vector2D {
	private float x;
	private float y;
	
	public Vector2D(Point point) {
		this((float)point.getX(), (float)point.getY());
	}
	
	public Vector2D(float x, float y) {
		this.set(x, y);
	}
	
	public Vector2D set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Vector2D add(Vector2D vec) {
		return this.add(vec.getX(), vec.getY());
	}
	
	public Vector2D add(float x, float y) {
		this.x += x;
		this.y += y;
		
		return this;
	}
	
	
}
