/**
 * 
 */
package io.aerodox.desktop.imitation;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;


/**
 * @author maeglin89273
 *
 */
public class Performer {
	private Robot delegate;
	
	private final Dimension screenSize = null;
	
	public Performer() {
		
		
		try {
			this.delegate = new Robot();
			
			
		} catch (AWTException e) {
//			e.printStackTrace();
			System.out.println("Aerodox is supported on this machine");
		}
	}
	
	public synchronized void mouseButton(MouseButtonState state) {
		if (state.isPress()) {
			this.delegate.mousePress(state.getMask());
		} else {
			this.delegate.mouseRelease(state.getMask());
		}
	}
	
	public synchronized boolean mouseMove(IntXY pos) {
		this.delegate.mouseMove(pos.getX(), pos.getY());
		
		return false;
//		return this.isOutOfMonitor(x, y);
		
	}
	
	private boolean isOutOfMonitor(int x, int y) {
		if (x < 0 || y < 0 || x > this.screenSize.getWidth() || y > this.screenSize.getHeight()) {
			return true;
		}
		
		return false;
	}
	
	
	
	public synchronized void mouseWheel(IntXY delta) {

		if (delta.getX() != 0) {
//			this.handleMouseWheelHorizontal(delta.getX());
		}
		
		if (delta.getY() != 0) {
			this.delegate.mouseWheel(delta.getY());
		}
		
	}
	
	private void handleMouseWheelHorizontal(int delta) {
		int btnMask = InputEvent.getMaskForButton(delta < 0? 4: 5), iterCount = Math.abs(delta);
		for (int i = 0; i < iterCount; i++) {
			this.delegate.mousePress(btnMask);
			this.delegate.mouseRelease(btnMask);
			
		}
	}
	
}
