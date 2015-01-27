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
	
	public Vector2D() {
		this(0, 0);
	}
	
	public Vector2D(double x, double y) {
	    this.x = x;
	    this.y = y;
	    
	}
	
	public Vector2D(Point point) {
		this.x = point.getX();
		this.y = point.getY();
	}
	
	public Vector2D(Vector2D v) {
		this(v.getX(),  v.getY());
	}
	
	public Vector2D(double radius, double radian, boolean anticlockwise) {
	    this(radius * Math.cos(radian), radius * Math.sin((anticlockwise? -1: 1) * radian));
	}
	
	public Vector2D setInPolarCoordinate(double r, double radian) {
	    this.set(r * Math.cos(radian), r * Math.sin(radian));
	    return this;
	}
	/**
	 * @param x the x to set
	 */
	
	public Vector2D setX(double x) {
		this.x  =  x;
		return this;
	}
	/**
	 * @return the x
	 */
	public double getX() {
		return this.x;
	}
	/**
	 * @param y the y to set
	 */
	public Vector2D setY(double y) {
	    this.y  =  y;
	    return this;
	}
	/**
	 * @return the y
	 */
    public double getY() {
    	return this.y;
    }
    
    public Vector2D add(Vector2D v) {
    	add(v.getX(), v.getY());
    	return this;
    }
    
    public Vector2D add(double x, double y) {
    	this.x += x;
    	this.y += y;
    	return this;
    }
    
    public Vector2D addMagnitude(double mag) {
    	setMagnitude(getMagnitude() + mag);
    	return this;
    }
    
    public static Vector2D add(Vector2D a, Vector2D b) {
    	return new Vector2D(a.getX() + b.getX(), a.getY()+b.getY());
    }
    
    public Vector2D set(Vector2D v) {
		this.x = v.getX();
		this.y = v.getY();
		return this;
    }
    
    public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
    }
    
    public double getSquare() {
    	return this.x * this.x + this.y * this.y;
    }
    
    public double getMagnitude() {
    	return Math.sqrt(this.getSquare());
    }
    
    public Vector2D setMagnitude(double mag) {
		double magLocal = getMagnitude();
		this.x *= mag / magLocal;
		this.y *= mag / magLocal;
		return this;
    }
    
    public double dotProduct(Vector2D v) {
    	return this.x * v.getX() + this.y * v.getY();
    }
    
    public static double dotProduct(Vector2D a , Vector2D b) {
    	return a.getX() * b.getX() + a.getY() * b.getY();
    }
    
    public double crossProduct(Vector2D v) {
    	return this.y * v.getX() - this.x *v.getY();
    }
    
    public static double crossProduct(Vector2D a, Vector2D b) {
    	return a.getY() * b.getX() - a.getX() * b.getY();
    }
    
    public Vector2D divided(double divisor) {
		this.x /= divisor;
		this.y /= divisor;
		return this;
    }
    
    public static Vector2D divided(Vector2D a, double divisor) {
    	return new Vector2D(a.getX() / divisor, a.getY() / divisor);
    }
    
    public Vector2D mutiply(double m) {
		this.x *= m;
		this.y *= m;
		return this;
    }
    
    public static Vector2D mutiply(Vector2D a, double m) {
    	return new Vector2D(a.getX() * m, a.getY() * m);
    }
    
    public Vector2D minus(Vector2D v) {
    	minus(v.getX(), v.getY());
    	return this;
    }
    
    public Vector2D minus(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
    }
    
    public Vector2D minusMagnitude(double mag) {
    	setMagnitude(getMagnitude() - mag);
    	return this;
    }
    
    public static Vector2D minus(Vector2D a, Vector2D b) {
    	return new Vector2D(a.getX() - b.getX(), a.getY() - b.getY());
    }
    
    public Vector2D reverse() {
		this.x = -this.x;
		this.y = -this.y;
		return this;
    }
    
    public static Vector2D reverse(Vector2D v) {
    	return new Vector2D(-v.getX(), -v.getY());
    }
    
    public Vector2D rotate(double deltaInRadians) {
		double absAngle = getAngle() + deltaInRadians;
		double r = getMagnitude();
		set(r * Math.cos(absAngle), r * Math.sin(absAngle));
		return this;
    }
    
    public Vector2D normalized() {
    	this.divided(getMagnitude());
    	return this;
    }
    
    public double getAngle() {
    	return Math.atan2(this.y,  this.x);
    }
    
    public Vector2D clone() {
    	return new Vector2D(this);
    }
    
    @Override
	public String toString() {
		return "x: " + this.x + " y: " + this.y;
	}
}
