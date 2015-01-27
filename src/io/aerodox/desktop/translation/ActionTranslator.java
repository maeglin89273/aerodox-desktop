/**
 * 
 */
package io.aerodox.desktop.translation;

/**
 * @author maeglin89273
 *
 */

// any subclasses should provide an zero-argument constructor
interface ActionTranslator {
	public abstract Action translate(Arguments args);
}
