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

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translate(io.aerodox.desktop.translation.Arguments)
	 */
	@Override
	public Action translate(Arguments args) {
		Vector2D ptg = args.getAsVector2D("posPtg");
		ConfigurationService config = ConfigurationService.getInstance();
		return new TouchAction(ptg.set(ptg.getX() * config.getScreenWidth() , ptg.getY() * config.getScreenHeight()));
	}
	
	private class TouchAction implements Action {
		
		Vector2D pos;
		
		private TouchAction(Vector2D acc) {
			this.pos = acc;
		}
		
		@Override
		public Object perform(Performer performer, Environment env) {
			performer.mouseMove(pos);
			return null;
		}
		
		
	}
}
