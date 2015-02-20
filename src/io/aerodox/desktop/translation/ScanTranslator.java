/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.service.Configuration;
import io.aerodox.desktop.service.ConfigurationGetter;

/**
 * @author maeglin89273
 *
 */
public class ScanTranslator implements ActionTranslator {

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.translation.ActionTranslator#translate(io.aerodox.desktop.translation.Arguments, io.aerodox.desktop.service.ConfigurationGetter)
	 */
	@Override
	public Action translate(Arguments args, ConfigurationGetter config) {
		
		return new ScanAction();
	}
	
	private static class ScanAction implements Action {
		
		@Override
		public JsonResponse perform(Performer performer, MotionTools tools, Configuration config) {
			return new ScanResponse(config.getHostname());
		}
		
	}
	
	private static class ScanResponse extends JsonResponse {
		private final String host;
		public ScanResponse(String hostname) {
			super("scan");
			host = hostname;
		}
		
	}
}
