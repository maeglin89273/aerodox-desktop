/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.MouseButtonState;
import io.aerodox.desktop.imitation.Performer;

/**
 * @author maeglin89273
 *
 */
public class MouseButtonTranslator implements SubTranslator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translateImpl(io.aerodox.desktop.translation.SubTranslator.Arguments)
	 */
	@Override
	public Action translate(Arguments args) {
		return new MouseButtonAction(args.asMouseButton("button"));
	}
	
	private class MouseButtonAction implements Action {
		private MouseButtonState btnState;
		
		private MouseButtonAction(MouseButtonState btnState) {
			this.btnState = btnState;
			
		}
		@Override
		public Object perform(Performer performer) {
			performer.mouseButton(this.btnState);
			return null;
		}
		
	}
}
