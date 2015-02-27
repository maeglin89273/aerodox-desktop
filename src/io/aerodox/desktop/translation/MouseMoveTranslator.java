/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.IntXY;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.imitation.motiontools.VirtualPointer;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;
import io.aerodox.desktop.test.MotionTracker;


/**
 * @author maeglin89273
 *
 */
public class MouseMoveTranslator implements ActionTranslator {
	private static final double BASELINE = 0.009;
	private static final float STRETCH_FACTOR = 1.1f;
	
	private static final double THRESHOLD = MathUtility.EPSILON * 3000;
	private static final MotionTracker tracker = new MotionTracker(6);
	
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {

		Vector3D rotVec = args.getAsVector3D("gyro");
		stretchVector(rotVec);
        double omegaMagnitude = rotVec.getMagnitude();
        if (omegaMagnitude < THRESHOLD) {
        	omegaMagnitude = 0;
        } else {
        	rotVec.divided(omegaMagnitude);
        }
        
        double thetaOverTwo = omegaMagnitude / 2.0f;
        double sinThetaOverTwo = Math.sin(thetaOverTwo);
        
        rotVec.mutiply(sinThetaOverTwo);
        
		double[][] rotMat = MathUtility.getRotationMatrixFromVector(rotVec);

		
		return new MouseMoveAction(rotMat);
	}
	
	
	private static void stretchVector(Vector3D vec) {
		vec.set(stretchScalar(vec.getX()), stretchScalar(vec.getY()), stretchScalar(vec.getZ()));
	}
	
	private static double stretchScalar(double scalar) {
		double sign = Math.signum(scalar);
		scalar = Math.abs(scalar);
		scalar = (scalar - BASELINE) * STRETCH_FACTOR + BASELINE;
		if (scalar < 0) {
			scalar = 0;
		}
		
		return sign * scalar;
	}
	
	private static class MouseMoveAction implements Action {
		private double[][] rotMat;
		
		private MouseMoveAction(double[][] rotMat) {
			this.rotMat = rotMat;
		}
		
		@Override
		public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
			tools.getMotionScroller().stopInertiaScroll();
			VirtualPointer pointer = tools.getVirtualPointer();
			pointer.setRotation(rotMat);
			IntXY pos = pointer.beamToScreen(config.getScreenPlane(), Configuration.getScreenSize());
			
			performer.mouseMove(pos);
			return null;
		}
	
	}
	
	
}
