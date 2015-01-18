/**
 * 
 */
package io.aerodox.desktop.math;

/**
 * @author maeglin89273
 *
 */
public class Vector3D {
	private double x;
	private double y;
	private double z;

	public Vector3D(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	public Vector3D(Vector3D v) {
		this(v.getX(), v.getY(), v.getZ());
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

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void add(Vector3D v) {
		add(v.getX(), v.getY(), v.getZ());
	}

	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public static Vector3D add(Vector3D a, Vector3D b) {
		return new Vector3D(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
	}

	public void addMagnitude(double mag) {
		setMagnitude(getMagnitude() + mag);
	}

	public void set(Vector3D v) {
		this.set(v.getX(), v.getY(), v.getZ());
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getSquare() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public double getMagnitude() {
		return Math.sqrt(getSquare());
	}

	public void setMagnitude(double mag) {
		double magLocal = getMagnitude();
		this.x *= mag / magLocal;
		this.y *= mag / magLocal;
		this.z *= mag / magLocal;
	}

	public double dotProduct(Vector3D v) {
		return this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
	}

	public Vector3D crossProduct(Vector3D v) {
		return new Vector3D(this.getY() * v.getZ() - this.getZ() * v.getY(),
							this.getZ() * v.getX() - this.getX() * v.getZ(),
							this.getX() * v.getY() - this.getY() * v.getX());
	}

	public void divided(double divisor) {
		this.x /= divisor;
		this.y /= divisor;
		this.z /= divisor;
	}

	public static Vector3D divided(Vector3D v, double divisor) {
		return new Vector3D(v.getX() / divisor, v.getY() / divisor, v.getZ() / divisor);
	}

	public void mutiply(double multiplicand) {
		this.x *= multiplicand;
		this.y *= multiplicand;
		this.z *= multiplicand;
	}

	public static Vector3D mutiply(Vector3D v, double multiplicand) {
		return new Vector3D(v.getX() * multiplicand, v.getY() * multiplicand, v.getZ() * multiplicand);
	}

	public void minus(Vector3D v) {
		minus(v.getX(), v.getY(), v.getZ());
	}

	public void minus(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	
	public static Vector3D minus(Vector3D a, Vector3D b) {
		return new Vector3D(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
	}
	
	public void minusMagnitude(double mag) {
		setMagnitude(getMagnitude() - mag);
	}

	public void reverse() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	public static Vector3D reverse(Vector3D v) {
		return new Vector3D(-v.getX(), -v.getY(), v.getZ());
	}

	public void rotate(Vector3D axis, double thetaInRadians) {
		axis = Vector3D.normalized(axis);
		double sin = Math.sin(thetaInRadians);
		double cos = Math.cos(thetaInRadians);
		double oneMCos = 1 - cos;
		
		double aX = axis.getX();
		double aY = axis.getY();
		double aZ = axis.getZ();
		
		this.x = this.x * (cos + aX * aX * oneMCos) + this.y * (aX * aY * oneMCos - aZ * sin) + this.z * (aX * aZ * oneMCos);
		this.y = this.x * (aY * aX * oneMCos + aZ * sin) + this.y * (cos + aY * aY * oneMCos) + this.z * (aY * aZ * oneMCos - aX * sin);
		this.z = this.x * (aZ * aX * oneMCos + aY * sin) + this.y * (aZ * aY * oneMCos + aX * sin) + this.z * (cos + aZ * aZ * oneMCos);
	}

	public Vector2D projectToPlane(Vector3D origin, Vector3D e1, Vector3D e2) {
		Vector3D delta = Vector3D.minus(this, origin);
		return new Vector2D(e1.dotProduct(delta), e2.dotProduct(delta));
	}
	
	public void normalized() {
		double magnitude = getMagnitude();
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
	}
	
	public static Vector3D normalized(Vector3D v) {
		double magnitude = v.getMagnitude();
		return new Vector3D(v.getX() / magnitude, v.getY() / magnitude, v.getZ() / magnitude);
	}
	
	@Override
	public Vector3D clone() {
		return new Vector3D(this);
	}
}
