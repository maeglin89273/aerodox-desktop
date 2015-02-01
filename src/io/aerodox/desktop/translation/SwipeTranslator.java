/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.VirtualPointer;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;

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
	
	private class SwipeAction implements Action {
		private Vector3D gyro;
		
		private SwipeAction(Vector3D gyro) {
			this.gyro = gyro;
			
		}
		@Override
		public Object perform(Performer performer, VirtualPointer pointer, Configuration config) {
			System.out.println("performing swipe");
			return null;
		}
		
	}
}
