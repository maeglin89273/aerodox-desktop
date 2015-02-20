/**
 * 
 */
package io.aerodox.desktop.test;

import io.aerodox.desktop.math.Vector3D;

/**
 * @author maeglin89273
 *
 */
public class MotionTracker {
	private static final double NS2S = 1 / 1000000000.0;
	private final long start;
	private String singleVecTamplate;
	private String valueFormat;
	
	public MotionTracker(int floatingPoint) { 
		this.start = System.nanoTime();
//		System.out.println("time, x, y, z");
		buildTamplate(floatingPoint);
	}
	
	private void buildTamplate(int floatingPoint) {
		valueFormat = "%." + floatingPoint + "f";
		StringBuilder tamplateBuilder = new StringBuilder();
		
		for (int i = 0; i < 3; i++) {
			tamplateBuilder.append(", ");
			tamplateBuilder.append(valueFormat);
		}
		this.singleVecTamplate = tamplateBuilder.toString();
	}

	public void track(Vector3D... vec) {
		double time = (System.nanoTime() - start) * NS2S;
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(valueFormat, time));
		for (int i = 0; i < vec.length; i++) {
			sb.append(String.format(singleVecTamplate, vec[i].getX(), vec[i].getY(), vec[i].getMagnitude()));
		}
		System.out.println(sb.toString());
	}
	
}
