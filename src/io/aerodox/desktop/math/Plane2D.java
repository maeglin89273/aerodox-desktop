/**
 * 
 */
package io.aerodox.desktop.math;

/**
 * @author maeglin89273
 *
 */
public class Plane2D {
	private Vector3D origin;
	private Vector3D e1;
	private Vector3D e2;
	
	public Plane2D(Vector3D origin, Vector3D e1, Vector3D e2) {
		this.setOrigin(origin);
		this.setE1(e1);
		this.setE2(e2);
		
	}

	public Vector3D getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3D origin) {
		this.origin = origin;
	}

	public Vector3D getE1() {
		return e1;
	}

	public void setE1(Vector3D e1) {
		this.e1 = e1;
	}

	public Vector3D getE2() {
		return e2;
	}

	public void setE2(Vector3D e2) {
		this.e2 = e2;
	}
}
