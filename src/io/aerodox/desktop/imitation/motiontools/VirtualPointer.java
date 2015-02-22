/**
 * 
 */
package io.aerodox.desktop.imitation.motiontools;

import io.aerodox.desktop.imitation.IntXY;
import io.aerodox.desktop.math.LowPassFilter;
import io.aerodox.desktop.math.LowPassFilter.ValueIterator;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Plane2D;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

import java.awt.MouseInfo;
import java.awt.Point;

/**
 * @author maeglin89273
 *
 */
public class VirtualPointer {
	
	
	private Vector3D position;
	private Vector3D panning;
	
	private double[][] rotDelta;
	private final LowPassFilter<double[][]> filter;
	
	private Vector3D velocity;
	private Vector3D lastRayPoint;
	
	
	public VirtualPointer() {
		this.panning = null;
		this.rotDelta = null;
		this.filter = new LowPassFilter<double[][]>(new MatrixIterator(new double[3][3]));
		
		this.position = new Vector3D();
		this.velocity = new Vector3D();
	}

	public void setRotation(double[][] rotMatrix) {
		this.rotDelta = filterRotMatrix(rotMatrix);
	}

	private double[][] filterRotMatrix(double[][] rotMatrix) {
		rotMatrix = filter.filter(new MatrixIterator(rotMatrix));
		if (filter.isStable()) {
			return rotMatrix;
		}
		return null;
	}
	
	public void pan(Vector3D vec) {
		this.panning = vec;
	}
	
	
	public IntXY beamToScreen(Plane2D screenPlane, Vector2D bounds) {
		Vector3D currentRay = this.getCurrentRay(screenPlane);
		
		rotateRay(currentRay, screenPlane);
		panRay(currentRay);
		
		Vector2D pointerOnScreen = currentRay.projectToPlane(screenPlane);
		
		updateVelocity(currentRay);
		return new IntXY(pointerOnScreen.getX(), pointerOnScreen.getY());
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
	
	private class MatrixIterator extends ValueIterator<double[][]> {
		private int i, j;
		
		public MatrixIterator(double[][] matrix) {
			super(matrix);
		}
		
		@Override
		public void restart() {
			this.i = this.j = 0;
			
		}

		@Override
		public boolean hasNext() {
			return this.i < this.values.length;
		}

		@Override
		public void next() {
			this.j++;
			if (this.j >= this.values[i].length) {
				this.j = 0;
				this.i++;
			}
		}

		@Override
		public void set(double value) {
			this.values[i][j] = value;
		}

		@Override
		public double get() {
			return this.values[i][j];
		}

		
	}

}
