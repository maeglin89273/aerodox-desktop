/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.IntXY;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.imitation.motiontools.VirtualPointer;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;
import io.aerodox.desktop.test.DelayEstimator;
import io.aerodox.desktop.test.DelayEstimator.Unit;


/**
 * @author maeglin89273
 *
 */
public class MouseMoveTranslator implements ActionTranslator {
	
	
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {

		Vector3D rotVec = args.getAsVector3D("rot");
		double[][] rotMat = MathUtility.getRotationMatrixFromVector(rotVec);

		
		return new MouseMoveAction(rotMat);
	}
	
	
	
	private static class MouseMoveAction implements Action {
		
		private double[][] rotMat;
		private static DelayEstimator est = new DelayEstimator(100, Unit.US);
		private MouseMoveAction(double[][] rotMat) {
			this.rotMat = rotMat;
		}
		
		@Override
		public Object perform(Performer performer, MotionTools tools, Configuration config) {
			VirtualPointer pointer = tools.getVirtualPointer();
			pointer.setRotation(rotMat);
			IntXY pos = pointer.beamToScreen(config.getScreenPlane(), Configuration.getScreenSize());
			
			performer.mouseMove(pos);
			return null;
		}
	
	}
	
	
}
