/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.Environment;
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
		return new MouseButtonAction(args.getAsMouseButton("btnState"));
	}
	
	private class MouseButtonAction implements Action {
		private MouseButtonState btnState;
		
		private MouseButtonAction(MouseButtonState btnState) {
			this.btnState = btnState;
			
		}
		@Override
		public Object perform(Performer performer, Environment env) {
//			System.out.println("performing button" + this.btnState.isPress());
			performer.mouseButton(this.btnState);
			return null;
		}
		
	}
}
