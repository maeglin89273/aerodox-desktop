/**
 * 
 */
package io.aerodox.desktop.imitation;

import java.awt.MouseInfo;
import java.awt.Point;

import io.aerodox.desktop.math.LowPassMatrixFilter;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Plane2D;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

/**
 * @author maeglin89273
 *
 */
public class VirtualPointer {
	
	
	private Vector3D position;
	private Vector3D panning;
	
	private double[][] preRot;
	private double[][] rotDelta;
	private LowPassMatrixFilter filter;
	
	private Vector3D velocity;
	private Vector3D lastRayPoint;
	
//	private Vector3D testVec = new Vector3D(0, 1, 0);
	
	public VirtualPointer() {
		this.panning = null;
		this.rotDelta = null;
		this.filter = new LowPassMatrixFilter();
		
		this.position = new Vector3D();
		this.velocity = new Vector3D();
	}
	
	public void retrackRotation() {
		this.preRot = null;
	}
	public void setRotation(double[][] rotMatrix) {
		if (this.preRot != null) {
			this.rotDelta = this.computeRotateDelta(rotMatrix);
		}
		
		this.preRot = rotMatrix;
	}
	
	private double[][] computeRotateDelta(double[][] rotMatrix) {
		double[][] delta = MathUtility.multipyMatrices(MathUtility.transposeMatrix(preRot), rotMatrix);
		delta = filter.filter(delta);
		if (filter.isStable()) {
			return delta;
		}
		return null;
	}
	
	
	public void pan(Vector3D vec) {
		this.panning = vec;
	}
	
	
	public Vector2D beamToScreen(Plane2D screenPlane, Vector2D bounds) {
		Vector3D currentRay = this.getCurrentRay(screenPlane);
		
		rotateRay(currentRay, screenPlane);
		panRay(currentRay);
		
		Vector2D pointerOnScreen = currentRay.projectToPlane(screenPlane);
		
		updateVelocity(currentRay);
		return pointerOnScreen;
	}
	
	private void rotateRay(Vector3D ray, Plane2D screenPlane) {
		if (rotDelta != null) {
			ray.applyMatrix(rotDelta);
			ray.mutiply(this.computeRayScalar(screenPlane, ray));
			rotDelta = null;
		}
		
	}
	
	private void panRay(Vector3D ray) {
		if (this.panning != null) {
			ray.add(this.panning);
			this.panning = null;
		}
		
	}
	
	private void updateVelocity(Vector3D rayPoint) {
		if (this.lastRayPoint != null) {
			this.velocity = Vector3D.minus(rayPoint, this.lastRayPoint);
		}
		this.lastRayPoint = rayPoint;
	}


	public Vector3D getVelocity() {
		return this.velocity;
	}
	
	/*
	 * formula is:
	 * (n * (o - pos)) / (n * dir)
	 */
	private double computeRayScalar(Plane2D plane, Vector3D ray) {
		Vector3D normal = plane.getNormal();
		double denominator = normal.dotProduct(ray);
		if (Math.abs(denominator) <= MathUtility.EPSILON) {
			return 0;
		}
		return normal.dotProduct(Vector3D.minus(plane.getOrigin(), this.position)) / denominator;
	}
	
	private Vector3D getCurrentRay(Plane2D screenPlane) {
		Point pos = MouseInfo.getPointerInfo().getLocation();
		Vector3D posIn3D = screenPositionTo3DPosition(screenPlane, pos);
		return posIn3D.minus(this.position);
	}
	
	private Vector3D screenPositionTo3DPosition(Plane2D screenPlane, Point pos) {
		Vector3D origin = screenPlane.getOrigin().clone();
		Vector3D translateX = Vector3D.mutiply(screenPlane.getE1(), pos.getX()); 
		Vector3D translateY = Vector3D.mutiply(screenPlane.getE2(), pos.getY());
		
		return origin.add(translateX).add(translateY);
	}
	
}
