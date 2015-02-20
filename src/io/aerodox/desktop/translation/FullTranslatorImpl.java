/**
 * 
 */
package io.aerodox.desktop.translation;

/**
 * @author maeglin89273
 *
 */
public class FullTranslatorImpl extends Translator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.Translator#register()
	 */
	@Override
	protected void register() {
		this.addTranslatorMapping("button", MouseButtonTranslator.class);
		this.addTranslatorMapping("config", ConfigurationTranslator.class);
		this.addTranslatorMapping("scan", null);
		
		this.addTranslatorMapping("move", MouseMoveTranslator.class);
		this.addTranslatorMapping("touch", TouchTranslator.class);
		this.addTranslatorMapping("swipe", SwipeTranslator.class);
	}

}
