/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.imitation.motiontools.VirtualPointer;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;

/**
 * @author maeglin89273
 *
 */
public class TouchTranslator implements ActionTranslator {
	private static final double SCALE = 0.9;
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translate(io.aerodox.desktop.translation.Arguments)
	 */
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		Vector2D mov = args.getAsVector2D("touchMov");
		double scale = (config.getSensitivity() + 1) * SCALE;
		return new TouchAction(mov.mutiply(scale));
	}
	
	private class TouchAction implements Action {
		
		private Vector3D mov;
		
		private TouchAction(Vector2D mov) {
			this.mov = new Vector3D(mov.getX(), 0, -mov.getY());
		}
		
		@Override
		public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
			VirtualPointer pointer = tools.getVirtualPointer();
			pointer.pan(mov);
			performer.mouseMove(pointer.beamToScreen(config.getScreenPlane(), Configuration.getScreenSize()));
			return null;
		}
		
		
	}
}
