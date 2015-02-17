/**
 * 
 */
package io.aerodox.desktop.imitation;

/**
 * @author maeglin89273
 *
 */
public class IntXY {
	private int x;
	private int y;
	
	public IntXY() {
		this(0, 0);
	}
	
	public IntXY(double x, double y) {
		this(round(x), round(y));
	}
	
	public IntXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	private static int round(double v) {
		return (int)Math.round(v);
	}
}
