/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;
import io.aerodox.desktop.service.MonitoringService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;

/**
 * @author maeglin89273
 *
 */
public class ConfigurationTranslator implements ActionTranslator {
	private static final Map<String, Configurator> CONFIG_MAP = new HashMap<String, Configurator>();
	
	static {
		CONFIG_MAP.put("sensitivity", new Configurator() {
			@Override
			public void configurate(Configuration config, JsonElement value) {
				int level = value.getAsInt();
				config.setSensitivity(level);
				MonitoringService.getInstance().update("sensitivity", level);
			}
		});
	}
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translateToAction(io.aerodox.desktop.translation.SubTranslator.Arguments)
	 */
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		List<Pair> configPairs = new LinkedList<Pair>();
		for (Entry<String, JsonElement> pair: args.getRaw().entrySet()) {
			configPairs.add(new Pair(CONFIG_MAP.get(pair.getKey()), pair.getValue()));
		}
		return new ConfigurationAction(configPairs);
	}
	
	private interface Configurator {
		public abstract void configurate(Configuration config, JsonElement value);
	}
	
	private static class Pair {
		private Configurator configurator;
		private JsonElement value;
		private Pair(Configurator configurator, JsonElement value) {
			this.configurator = configurator;
			this.value = value;
			
		}
	}
	
	private static class ConfigurationAction implements Action {
		private List<Pair> configPairs;
		public ConfigurationAction(List<Pair> configPairs) {
			this.configPairs = configPairs;
		}

		@Override
		public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
			for (Pair configPair: this.configPairs) {
				configPair.configurator.configurate(config, configPair.value);
			}
			return null;
		}
	}
}
