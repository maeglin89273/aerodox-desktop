/**
 * 
 */
package io.aerodox.desktop.imitation.motiontools;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.imitation.IntXY;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.math.LowPassFilter;
import io.aerodox.desktop.math.LowPassFilter.ValueIterator;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.PerformingService;
import io.aerodox.desktop.translation.Action;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author maeglin89273
 *
 */
public class MotionScroller {
	private static final float RADIUS = 8f;
	private static final float DIAMETER = 2 * RADIUS;
	private static final float RELEASE_ANGULAR_VOL = 0.0585f;
	private static final float RELEASE_ANGULAR_VOL_SQUARE = RELEASE_ANGULAR_VOL * RELEASE_ANGULAR_VOL;
	
	private final IntXY noVol = new IntXY(); 
	private final LowPassFilter<double[]> filter;
	
	private final InertiaProducer inertiaProducer;
	
	private Vector3D distance;
	private int ix, iy, iz;

	
//	private static final MotionTracker TRACKER = new MotionTracker(6);
	
	public MotionScroller() {
		 this.filter = new LowPassFilter<double[]>(new VectorIterator(new double[3]), 0.83f);
		 this.inertiaProducer = new InertiaProducer();
		 
		 this.distance = new Vector3D();
		 this.ix = this.iy = this.iz = 0;
		 
	}
	
	public IntXY swipe(Vector3D angularVol) {

		if (this.inertiaProducer.isRunning()) {
			this.inertiaProducer.tryAccelerateV0(angularVol);
			return this.noVol;
		}
		
		IntXY scrollVol = this.rollToScroll(angularVol);
		
		if (angularVol.getSquare() > RELEASE_ANGULAR_VOL_SQUARE) {
			//schedule a new inertia buffer routine
			this.inertiaProducer.start(angularVol);
		}

		return scrollVol;
		
	}
	
	private IntXY rollToScroll(Vector3D angularVol) {
		filterVol(angularVol);
		int[] scrollVol = computeScrollVol(angularVol);
		return new IntXY(scrollVol[2], scrollVol[0]);
	}
	
	
	private void filterVol(Vector3D angularVol) {
		double[] volArray = filter.filter(new VectorIterator(new double[] {angularVol.getX(), angularVol.getY(), angularVol.getZ()}));
		angularVol.set(volArray[0], volArray[1], volArray[2]);
	}

	private int[] computeScrollVol(Vector3D angularVol) {
		int[] scrollVol = new int[] {ix, iy, iz};
		
		this.distance.add(Vector3D.mutiply(angularVol, DIAMETER));
		
		ix = (int) this.distance.getX();
		iy = (int) this.distance.getY();
		iz = (int) this.distance.getZ();
		scrollVol[0] = ix - scrollVol[0];
		scrollVol[1] = iy - scrollVol[1];
		scrollVol[2] = iz - scrollVol[2];
		
		return scrollVol;
	}

	private class VectorIterator extends ValueIterator<double[]> {
		private int i;
		
		public VectorIterator(double[] values) {
			super(values);
		}
		
		@Override
		public void restart() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < this.values.length;
		}

		@Override
		public void next() {
			i++;
		}

		@Override
		public void set(double value) {
			this.values[this.i] = value;
		}

		@Override
		public double get() {
			return this.values[this.i];
		}
		
	}
	
	private class InertiaProducer {
		private Timer inertiaSchduler;
		private Slower slower;
		private boolean running = false;
		
		public boolean isRunning() {
			return running;
		}
		 
		public void tryAccelerateV0(Vector3D angularVol) {
			if (angularVol.getSquare() > slower.getV0().getSquare()) {
				slower.setV0(angularVol);
			}
		}

		public void start(Vector3D v0) {
			this.inertiaSchduler = new Timer();
			this.slower = new Slower(v0, System.currentTimeMillis());
			this.inertiaSchduler.schedule(this.slower, AerodoxConfig.MOTION_UPDATE_PERIOD_IN_MS, AerodoxConfig.MOTION_UPDATE_PERIOD_IN_MS);
			this.running = true;
		}
		
		public void stop() {
			this.inertiaSchduler.cancel();
			this.running = false;
		}
		
		private class Slower extends TimerTask {
			
			private Vector3D v0;
			private long startTime;
			
			public Slower(Vector3D v0, long startTime) {
				this.v0 = v0;
				this.startTime = startTime;
			}
			
			@Override
			public void run() {
				Vector3D nextVol = this.slowdown();
				
				if (nextVol.getSquare() < MathUtility.EPSILON) {
					this.cancel();
					return;
				}
				
				PerformingService.getInstance().queueAction(new InertiaAction(nextVol), null);
			}
			
			public synchronized void setV0(Vector3D v0) {
				this.v0 = v0;
			}
			
			public synchronized Vector3D getV0() {
				return this.v0;
			}
			
			private Vector3D slowdown() {
				double time = (this.scheduledExecutionTime() - this.startTime) / 1000.0;
				Vector3D v0 = this.getV0();
				double nextVolX = MathUtility.slowdownFormula(time, v0.getX());
				double nextVolY = MathUtility.slowdownFormula(time, v0.getY());
				double nextVolZ = MathUtility.slowdownFormula(time, v0.getZ());
				
				return new Vector3D(nextVolX, nextVolY, nextVolZ);
			}
			
			@Override
			public boolean cancel() {
				boolean result = super.cancel();
				InertiaProducer.this.stop();
				return result;
				
			}
		}
	}
	
	private static class InertiaAction implements Action {
		private Vector3D vol;
		public InertiaAction(Vector3D nextVol) {
			this.vol = nextVol;
		}

		@Override
		public Object perform(Performer performer, MotionTools tools, Configuration config) {
			performer.mouseWheel(tools.getMotionScroller().rollToScroll(vol));
			return null;
		}
		
	}
}
