/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.ConfigurationService;


/**
 * @author maeglin89273
 *
 */
public class MouseMoveTranslator implements SubTranslator {
	
//	private static final Vector3D ORIGIN = new Vector3D();
//	private static final Vector3D E1;
//	private static final Vector3D E2;
//	
	@Override
	public Action translate(Arguments args) {
		Vector3D acc = args.getAsVector3D("acc");
//		Vector3D angle = args.asVector3D("angle");
		acc.mutiply(ConfigurationService.getInstance().getSensitivity());
		
		return new MouseMoveAction(new Vector2D(acc.getX(), acc.getY()));
	}
	
	private class MouseMoveAction implements Action {
		
		Vector2D pos;
		
		private MouseMoveAction(Vector2D pos) {
			this.pos = pos;
		}
		
		@Override
		public Object perform(Performer performer) {
			System.out.println("performing move");
			performer.mouseShift(pos);
			return null;
		}
		
		
	}

}
