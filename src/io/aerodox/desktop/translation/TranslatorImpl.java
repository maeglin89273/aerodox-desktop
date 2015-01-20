/**
 * 
 */
package io.aerodox.desktop.translation;

/**
 * @author maeglin89273
 *
 */
class TranslatorImpl extends Translator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.Translator#register()
	 */
	@Override
	protected void register() {
		this.addTranslatorMapping("move", MouseMoveTranslator.class);
		this.addTranslatorMapping("button", MouseButtonTranslator.class);
		this.addTranslatorMapping("swipe", SwipeTranslator.class);
		this.addTranslatorMapping("config", ConfigurationTranslator.class);
		this.addTranslatorMapping("touch", TouchTranslator.class);
	}

}
