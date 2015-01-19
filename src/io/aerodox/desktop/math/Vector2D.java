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
	private double x;
	private double y;
	
	public Vector2D(Point point) {
		this(point.getX(), point.getY());
	}
	
	public Vector2D(double x, double y) {
		this.set(x, y);
	}
	
	public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Vector2D add(Vector2D vec) {
		return this.add(vec.getX(), vec.getY());
	}
	
	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		
		return this;
	}
	
	
}
