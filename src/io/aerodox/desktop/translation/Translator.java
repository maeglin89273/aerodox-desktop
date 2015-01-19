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
	private Map<String, Class<? extends SubTranslator>> translatorMap;
	private ExecutorService threadPool;
	
	public Translator() {
		this.translatorMap = new HashMap<String, Class<? extends SubTranslator>>();
		this.threadPool = Executors.newCachedThreadPool();
		this.register();
	}
	
	public void asyncTranslate(JsonObject chunk, AsyncResponseChannel channel) {
		
		String action = chunk.remove("action").getAsString();
		try {
			Class<? extends SubTranslator> translatorClass = translatorMap.get(action);
			if (translatorClass != null) {
				this.asyncDispatch(translatorClass.newInstance(), chunk, channel);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void asyncDispatch(SubTranslator translator, JsonObject args, AsyncResponseChannel channel) {
		this.threadPool.execute(new TranslationTask(translator, args, channel));
	}
	
	public void stopTranslation() {
		this.threadPool.shutdown();
	}
	
	protected void addTranslatorMapping(String name, Class<? extends SubTranslator> translatorClass) {
		this.translatorMap.put(name, translatorClass);
	}
	
	protected abstract void register();
	
	public static Translator newTranslator() {
		return new TranslatorImpl();
	}
	
	private class TranslationTask implements Runnable {

			private SubTranslator translator;
			private JsonObject args;
			private AsyncResponseChannel channel;
			
			private TranslationTask(SubTranslator translator, JsonObject args, AsyncResponseChannel channel) {
				this.translator = translator;
				this.args = args;
				this.channel = channel;
				
			}
			
			@Override
			public void run() {
				Action action = translator.translate(new Arguments(args));
				if (action != null) {
					PerformingService.getInstance().queueAction(action, channel);
				}
			}
			
	}
}
