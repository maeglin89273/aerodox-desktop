/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.service.PerformingService;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author maeglin89273
 *
 */
public abstract class Translator {
	public enum Type {MOTION, COMMAND};
	
	private Map<String, Class<? extends ActionTranslator>> translatorMap;
	private ExecutorService threadPool;
	
	public Translator() {
		this.translatorMap = new HashMap<String, Class<? extends ActionTranslator>>();
		this.threadPool = Executors.newCachedThreadPool();
		this.register();
	}
	
	public void asyncTranslate(JsonObject chunk, AsyncResponseChannel channel) {
		
		String action = chunk.remove("act").getAsString();
		try {
			Class<? extends ActionTranslator> translatorClass = translatorMap.get(action);
			if (translatorClass != null) {
				this.asyncDispatch(translatorClass.newInstance(), chunk, channel);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void asyncDispatch(ActionTranslator translator, JsonObject args, AsyncResponseChannel channel) {
		this.threadPool.execute(new TranslationTask(translator, args, channel));
	}
	
	public void stopTranslation() {
		this.threadPool.shutdown();
	}
	
	protected void addTranslatorMapping(String name, Class<? extends ActionTranslator> translatorClass) {
		this.translatorMap.put(name, translatorClass);
	}
	
	protected abstract void register();
	
	public static Translator newTranslator(Type type) {
		switch (type) {
			case MOTION:
				return new MotionTranslatorImpl();
			case COMMAND:
				new CommandTranslatorImpl();
		}
		
		return null;
	}
	
	
	private class TranslationTask implements Runnable {

			private ActionTranslator translator;
			private JsonObject args;
			private AsyncResponseChannel channel;
			
			private TranslationTask(ActionTranslator translator, JsonObject args, AsyncResponseChannel channel) {
				this.translator = translator;
				this.args = args;
				this.channel = channel;
				
			}
			
			@Override
			public void run() {
				PerformingService service = PerformingService.getInstance();
				Action action = translator.translate(new Arguments(args), service.getConfigGetter());
				if (action != null) {
					service.queueAction(action, channel);
				}
			}
			
	}
}
