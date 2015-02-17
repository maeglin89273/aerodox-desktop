/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;
import io.aerodox.desktop.test.MotionTracker;

/**
 * @author maeglin89273
 *
 */
public class SwipeTranslator implements ActionTranslator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translateToAction(io.aerodox.desktop.translation.SubTranslator.Arguments)
	 */
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		return new SwipeAction(args.getAsVector3D("gyro"));
	}
	
	private static class SwipeAction implements Action {
		private Vector3D gyro;
		
		private SwipeAction(Vector3D gyro) {
			this.gyro = gyro;
			
		}
		
		
		@Override
		public Object perform(Performer performer, MotionTools tools, Configuration config) {
			tools.getVirtualPointer().retrackRotation();
			performer.mouseWheel(tools.getMotionScroller().swipe(gyro));
			return null;
		}
		
	}
}
