/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.Environment;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.service.ConfigurationService;

/**
 * @author maeglin89273
 *
 */
public class TouchTranslator implements SubTranslator {
	private static final double SCALE = 0.2;
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translate(io.aerodox.desktop.translation.Arguments)
	 */
	@Override
	public Action translate(Arguments args) {
		Vector2D mov = args.getAsVector2D("touchMov");
		ConfigurationService config = ConfigurationService.getInstance();
		return new TouchAction(mov.set(mov.getX() * SCALE , mov.getY() * SCALE));
	}
	
	private class TouchAction implements Action {
		
		Vector2D mov;
		
		private TouchAction(Vector2D mov) {
			this.mov = mov;
		}
		
		@Override
		public Object perform(Performer performer, Environment env) {
			performer.mouseMove(env.getMousePosition().add(mov));
			return null;
		}
		
		
	}
}
