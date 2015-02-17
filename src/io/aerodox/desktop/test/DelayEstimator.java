/**
 * 
 */
package io.aerodox.desktop.test;

/**
 * @author maeglin89273
 *
 */
public class DelayEstimator {
	
	public enum Unit {
		S(9), MS(6), US(3);
		private double overNano;
		
		private Unit(double expo) {
			this.overNano = Math.pow(10, expo);
		}
		
		public double getUnitOverNano() {
			return this.overNano;
		}
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	};
	
	private long threshold;
	private long startTime;
	private long lastDelayedTime; 
	private Unit unit;
	
	public DelayEstimator(long threshold, Unit unit) {
		this.threshold = threshold;
		this.unit = unit;
		this.lastDelayedTime = System.currentTimeMillis();
	}
	
	public void start() {
		this.startTime = System.nanoTime();
	}
	
	public void estimate() {
		double diff = (System.nanoTime() - this.startTime) / this.unit.getUnitOverNano();
		if (diff >= this.threshold) {
			long timestamp = System.currentTimeMillis();
			System.out.printf("delay about: %.3f%s\n", diff, unit.toString());
			System.out.printf("%.3fs past since the last delay\n", (timestamp - this.lastDelayedTime) / 1000.0);
			this.lastDelayedTime = timestamp;
		}
	}
}
