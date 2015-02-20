/**
 * 
 */
package io.aerodox.desktop.connection;

/**
 * @author maeglin89273
 *
 */
public abstract class JsonResponse {
	private final String rsp;
	protected JsonResponse(String header) {
		this.rsp = header;
	}
}
