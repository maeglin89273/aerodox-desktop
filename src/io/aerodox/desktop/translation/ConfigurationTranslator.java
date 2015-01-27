/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.service.ConfigurationService;

import java.util.HashMap;
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
			public void configurate(ConfigurationService config, JsonElement value) {
				config.setSensitivity(value.getAsInt());
			}
		});
		
		
	}
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.SubTranslator#translateToAction(io.aerodox.desktop.translation.SubTranslator.Arguments)
	 */
	@Override
	public Action translate(Arguments args) {
		ConfigurationService config = ConfigurationService.getInstance(); 
		for (Entry<String, JsonElement> pair: args.getRaw().entrySet()) {
			CONFIG_MAP.get(pair.getKey()).configurate(config, pair.getValue());;
		}
		return null;
	}
	
	private interface Configurator {
		public abstract void configurate(ConfigurationService config, JsonElement value);
	}
}
