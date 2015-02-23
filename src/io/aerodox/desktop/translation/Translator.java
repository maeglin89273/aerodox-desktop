/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.service.PerformingService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonObject;

/**
 * @author maeglin89273
 *
 */
public abstract class Translator {
	private Map<String, Class<? extends ActionTranslator>> actionTranslatorMap;
	private ExecutorService threadPool;
	
	public Translator() {
		this.actionTranslatorMap = new HashMap<String, Class<? extends ActionTranslator>>();
		this.threadPool = Executors.newCachedThreadPool();
		this.register();
	}
	
	public void asyncTranslate(JsonObject chunk, AsyncResponseChannel channel) {
		
		String action = chunk.remove("act").getAsString();
		try {
			Class<? extends ActionTranslator> translatorClass = actionTranslatorMap.get(action);
			if (translatorClass != null) {
				this.asyncDispatch(translatorClass.newInstance(), chunk, channel);
			} else {
				System.out.println("no action \"" + action + "\" found");
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
		this.actionTranslatorMap.put(name, translatorClass);
	}
	
	protected abstract void register();
	
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
