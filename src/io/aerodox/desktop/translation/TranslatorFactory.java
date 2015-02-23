/**
 * 
 */
package io.aerodox.desktop.translation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author maeglin89273
 *
 */
public class TranslatorFactory {
	public enum Type {FULL, MOTION, COMMAND}

	private static final Map<Type, Class<? extends Translator>> TRANSLATOR_IMPL_CLASSES;

	static {
		TRANSLATOR_IMPL_CLASSES = new HashMap<TranslatorFactory.Type, Class<? extends Translator>>(); 
		TRANSLATOR_IMPL_CLASSES.put(TranslatorFactory.Type.FULL, FullTranslatorImpl.class);
		TRANSLATOR_IMPL_CLASSES.put(TranslatorFactory.Type.MOTION, MotionTranslatorImpl.class);
		TRANSLATOR_IMPL_CLASSES.put(TranslatorFactory.Type.COMMAND, CommandTranslatorImpl.class);
	}

	private TranslatorFactory() {
		System.out.println("factory class should not be instantiated");
	}

	public static Translator newTranslator(Type type) {
		Class<? extends Translator> translatorClass = TRANSLATOR_IMPL_CLASSES.get(type);
		try {
			return translatorClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
