/**
 * 
 */
package io.aerodox.desktop.imitation.motiontools;

import io.aerodox.desktop.test.MotionTracker;

/**
 * @author maeglin89273
 *
 */
public class MotionTools {
	private final VirtualPointer pointer;
	private final MotionScroller scroller;
	
	
	public MotionTools() {
		this.pointer = new VirtualPointer();
		this.scroller = new MotionScroller();
	}
	
	public VirtualPointer getVirtualPointer() {
		return pointer;
	}

	public MotionScroller getMotionScroller() {
		return scroller;
	}
	
	
	
}
