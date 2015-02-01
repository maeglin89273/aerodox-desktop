/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.VirtualPointer;
import io.aerodox.desktop.imitation.MouseButtonState;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;

/**
 * @author maeglin89273
 *
 */
public class MouseButtonTranslator implements ActionTranslator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translateImpl(io.aerodox.desktop.translation.SubTranslator.Arguments)
	 */
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		return new MouseButtonAction(args.getAsMouseButton("btnState"));
	}
	
	private class MouseButtonAction implements Action {
		private MouseButtonState btnState;
		
		private MouseButtonAction(MouseButtonState btnState) {
			this.btnState = btnState;
			
		}
		@Override
		public Object perform(Performer performer, VirtualPointer pointer, Configuration config) {
			performer.mouseButton(this.btnState);
			return null;
		}
		
	}
}
