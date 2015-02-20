/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.connection.JsonResponse;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.imitation.motiontools.MotionTools;
import io.aerodox.desktop.service.Configuration;

/**
 * @author maeglin89273
 *
 */
public interface Action {
	public abstract JsonResponse perform(Performer performer, MotionTools tools, Configuration config);
}
