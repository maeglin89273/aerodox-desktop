/**
 * 
 */
package io.aerodox.desktop.translation;

import java.util.Timer;
import java.util.TimerTask;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.IntXY;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.imitation.motiontools.VirtualPointer;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;
import io.aerodox.desktop.service.ServiceManager;
import io.aerodox.desktop.test.MotionTracker;


/**
 * @author maeglin89273
 *
 */
public class SlowMouseMoveTranslator extends MouseMoveTranslator {
	private static final double BASELINE = 0.009;
	private static final float STRETCH_FACTOR = 1.1f;
	
	private static final double THRESHOLD = MathUtility.EPSILON * 3000;
	private static final MotionTracker tracker = new MotionTracker(6);
	
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		return new SmoothMoveAction((MouseMoveAction) super.translate(args, config));
	}
	
	
	private static class SmoothMoveAction implements Action {
		private MouseMoveAction wrapped;

		private static long lastActionTimestamp = 0;
		private static double[][] lastRot;
		private static Timer smoother;
		
		private SmoothMoveAction(MouseMoveAction wrapped) {
			this.wrapped = wrapped;
		}
		
		@Override
		public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
			
			produceSmoother(this.wrapped.getRotMat());
			this.wrapped.perform(performer, tools, config);
			return null;
		}
		
		private void produceSmoother(double[][] curRot) {
			if (smoother != null) {
				smoother.cancel();
			}
			
			long currentTimestamp = System.currentTimeMillis();
			long diff = currentTimestamp - lastActionTimestamp;
			
			smoother = new Timer();
			smoother.schedule(new PsudoMoveProducer(diff, lastRot, curRot), 33);
			lastRot = curRot;
			lastActionTimestamp = currentTimestamp;
		}
		
		private static class PsudoMoveProducer extends TimerTask {
			private static final double MAX_DELAY = 85.0;
			
			private double[][] preRot;
			private double[][] curRot;
			private long delay;
			
			private PsudoMoveProducer(long delay, double[][] preRot, double[][] curRot) {
				this.delay = delay;
				this.preRot = preRot;
				this.curRot = curRot;
			}
			
			@Override
			public void run() {
				double alpha = delay / MAX_DELAY;
				double[][] predictRot = this.curRot;
				if (alpha < 1) {
					predictRot = predict(alpha);
				}
				ServiceManager.performing().queueAction(new MouseMoveAction(predictRot));
			}
			
			private double[][] predict(double alpha) {
				double[][] predictRot = new double[3][3];
				double oneMinusAlpha = 1 - alpha;
				for (int i = 0; i < predictRot.length; i++) {
					for (int j = 0; j < predictRot[i].length; j++) {
						predictRot[i][j] = oneMinusAlpha * preRot[i][j] + alpha * curRot[i][j];
					}
				}
				
				return predictRot;
			}
		}
	}
	
	
}
