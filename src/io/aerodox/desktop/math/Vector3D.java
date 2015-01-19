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
	
	public Vector3D() {
		this(0, 0, 0);
	}
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

	public Vector3D setX(double x) {
		this.x = x;
		return this;
	}

	public double getY() {
		return y;
	}

	public Vector3D setY(double y) {
		this.y = y;
		return this;
	}

	public double getZ() {
		return z;
	}

	public Vector3D setZ(double z) {
		this.z = z;
		return this;
	}

	public Vector3D add(Vector3D v) {
		return add(v.getX(), v.getY(), v.getZ());
	}

	public Vector3D add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		
		return this;
	}

	public static Vector3D add(Vector3D a, Vector3D b) {
		return new Vector3D(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
	}

	public Vector3D addMagnitude(double mag) {
		return setMagnitude(getMagnitude() + mag);
	}

	public Vector3D set(Vector3D v) {
		return this.set(v.getX(), v.getY(), v.getZ());
	}

	public Vector3D set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}

	public double getSquare() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public double getMagnitude() {
		return Math.sqrt(getSquare());
	}
	
	public Vector3D setMagnitude(double mag) {
		double magLocal = getMagnitude();
		this.x *= mag / magLocal;
		this.y *= mag / magLocal;
		this.z *= mag / magLocal;
		
		return this;
	}

	public double dotProduct(Vector3D v) {
		return this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
	}

	public Vector3D crossProduct(Vector3D v) {
		return new Vector3D(this.getY() * v.getZ() - this.getZ() * v.getY(),
							this.getZ() * v.getX() - this.getX() * v.getZ(),
							this.getX() * v.getY() - this.getY() * v.getX());
	}

	public Vector3D divided(double divisor) {
		this.x /= divisor;
		this.y /= divisor;
		this.z /= divisor;
		
		return this;
	}

	public static Vector3D divided(Vector3D v, double divisor) {
		return new Vector3D(v.getX() / divisor, v.getY() / divisor, v.getZ() / divisor);
	}

	public Vector3D mutiply(double multiplicand) {
		this.x *= multiplicand;
		this.y *= multiplicand;
		this.z *= multiplicand;
		
		return this;
	}

	public static Vector3D mutiply(Vector3D v, double multiplicand) {
		return new Vector3D(v.getX() * multiplicand, v.getY() * multiplicand, v.getZ() * multiplicand);
	}

	public Vector3D minus(Vector3D v) {
		return minus(v.getX(), v.getY(), v.getZ());
	}

	public Vector3D minus(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		
		return this;
	}
	
	public static Vector3D minus(Vector3D a, Vector3D b) {
		return new Vector3D(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
	}
	
	public Vector3D minusMagnitude(double mag) {
		return setMagnitude(getMagnitude() - mag);
	}
	
	public Vector3D negateX() {
		this.x = -this.x;
		return this;
	}
	
	public Vector3D negateY() {
		this.y = -this.y;
		return this;
	}
	
	public Vector3D negateZ() {
		this.z = -this.z;
		return this;
	}
	
	public Vector3D reverse() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		
		return this;
	}

	public static Vector3D reverse(Vector3D v) {
		return new Vector3D(-v.getX(), -v.getY(), v.getZ());
	}
	
	public Vector3D round() {
		this.x = Math.round(x);
		this.y = Math.round(y);
		this.z = Math.round(z);
		
		return this;
	}
	
	public Vector3D rotate(Vector3D axis, double thetaInRadians) {
		axis = Vector3D.normalized(axis);
		double sin = Math.sin(thetaInRadians);
		double cos = Math.cos(thetaInRadians);
		double oneMCos = 1 - cos;
		
		double aX = axis.getX();
		double aY = axis.getY();
		double aZ = axis.getZ();
		double x = this.x, y = this.y, z = this.z;
		this.x = x * (cos + aX * aX * oneMCos) + y * (aX * aY * oneMCos - aZ * sin) + z * (aX * aZ * oneMCos);
		this.y = x * (aY * aX * oneMCos + aZ * sin) + y * (cos + aY * aY * oneMCos) + z * (aY * aZ * oneMCos - aX * sin);
		this.z = x * (aZ * aX * oneMCos + aY * sin) + y * (aZ * aY * oneMCos + aX * sin) + z * (cos + aZ * aZ * oneMCos);
		
		return this;
	}
	
	public Vector3D applyMatrix(double[] m) {
		double x = this.x, y = this.y, z = this.z;
		this.x = m[0] * x + m[1] * y + m[2] * z;
		this.y = m[3] * x + m[4] * y + m[5] * z;
		this.z = m[6] * x + m[7] * y + m[8] * z;
		
		return this;
	}
	
	public Vector2D projectToPlane(Plane2D plane) {
		Vector3D delta = Vector3D.minus(this, plane.getOrigin());
		return new Vector2D(plane.getE1().dotProduct(delta), plane.getE2().dotProduct(delta));
	}
	
	public Vector3D normalized() {
		double magnitude = getMagnitude();
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
		
		return this;
	}
	
	public static Vector3D normalized(Vector3D v) {
		double magnitude = v.getMagnitude();
		return new Vector3D(v.getX() / magnitude, v.getY() / magnitude, v.getZ() / magnitude);
	}
	
	@Override
	public Vector3D clone() {
		return new Vector3D(this);
	}
	
	@Override
	public String toString() {
		return "x: " + this.x + " y: " + this.y + " z:" + this.z;
	}
}
