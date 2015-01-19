/**
 * 
 */
package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.Environment;
import io.aerodox.desktop.imitation.Performer;

/**
 * @author maeglin89273
 *
 */
public interface Action {
	
	public abstract Object perform(Performer performer, Environment env);
}
