/**
 * 
 */
package io.aerodox.desktop.translation;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;

/**
 * @author maeglin89273
 *
 */
public class ArrowTranslator implements ActionTranslator {
	
	private static final Map<String, Integer> ARROW_KEY_MAPPER;
	static {
		ARROW_KEY_MAPPER = new HashMap<String, Integer>();
		ARROW_KEY_MAPPER.put("RIGHT", KeyEvent.VK_RIGHT);
		ARROW_KEY_MAPPER.put("LEFT", KeyEvent.VK_LEFT);
		ARROW_KEY_MAPPER.put("UP", KeyEvent.VK_UP);
		ARROW_KEY_MAPPER.put("DOWN", KeyEvent.VK_DOWN);
	}
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.ActionTranslator#translate(io.aerodox.desktop.translation.Arguments, io.aerodox.desktop.service.ConfigurationGetter)
	 */
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		int keycode = ARROW_KEY_MAPPER.get(args.getRaw().get("dir").getAsString());
		return new ArrowAction(keycode);
	}
	
	private static class ArrowAction implements Action {
		private int directionKeycode;

		public ArrowAction(int directionKeycode) {
			this.directionKeycode = directionKeycode;
		}

		@Override
		public JsonResponse perform(Performer performer, MotionTools tools,	Configuration config) {
			performer.keyTyped(directionKeycode);
			return null;
		}
		
	}

}
