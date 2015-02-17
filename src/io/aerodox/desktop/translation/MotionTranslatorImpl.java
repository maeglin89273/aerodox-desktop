/**
 * 
 */
package io.aerodox.desktop.translation;

/**
 * @author maeglin89273
 *
 */
class MotionTranslatorImpl extends Translator {


	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.Translator#register()
	 */
	@Override
	protected void register() {
		this.addTranslatorMapping("button", MouseButtonTranslator.class);
		this.addTranslatorMapping("move", MouseMoveTranslator.class);
		this.addTranslatorMapping("touch", TouchTranslator.class);
		this.addTranslatorMapping("swipe", SwipeTranslator.class);
	}

}
