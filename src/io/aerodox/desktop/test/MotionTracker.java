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
	private String tamplate;
	public MotionTracker(int floatingPoint) { 
		this.start = System.nanoTime();
		System.out.println("x, y, z");
		buildTamplate(floatingPoint);
	}
	
	private void buildTamplate(int floatingPoint) {
		String valueFormat = "%." + floatingPoint + "f";
		StringBuilder tamplateBuilder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			tamplateBuilder.append(valueFormat);
			tamplateBuilder.append(i < 3? ", ": "\n");
		}
		
		this.tamplate = tamplateBuilder.toString();
	}

	public void track(Vector3D vec) {
		double time = (System.nanoTime() - start) * NS2S;
		System.out.printf(tamplate, time, vec.getX(), vec.getY(), vec.getZ());
	}
	
}
