/**
 * 
 */
package io.aerodox.desktop.translation;

/**
 * @author maeglin89273
 *
 */
class CommandTranslatorImpl extends Translator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.Translator#register()
	 */
	@Override
	protected void register() {
		this.addTranslatorMapping("button", MouseButtonTranslator.class);
		this.addTranslatorMapping("config", ConfigurationTranslator.class);
		
	}

}
