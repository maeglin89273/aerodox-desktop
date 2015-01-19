/**
 * 
 */
package io.aerodox.desktop.imitation;

import io.aerodox.desktop.math.Vector2D;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
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
	
	public synchronized boolean mouseMove(Vector2D pos) {
		
		int x = round(pos.getX());
		int y = round(pos.getY());
		
		this.delegate.mouseMove(x, y);
		
		return false;
//		return this.isOutOfMonitor(x, y);
		
	}
	
	private boolean isOutOfMonitor(int x, int y) {
		if (x < 0 || y < 0 || x > this.screenSize.getWidth() || y > this.screenSize.getHeight()) {
			return true;
		}
		
		return false;
	}
	
	private int round(double v) {
		return (int)Math.round(v);
	}
	
	public synchronized void mouseWheel(int dx, int dy) {
		this.delegate.mouseWheel(dy);
		this.delegate.keyPress(InputEvent.SHIFT_DOWN_MASK);
		this.delegate.mouseWheel(dx);
		this.delegate.keyRelease(InputEvent.SHIFT_DOWN_MASK);
	}
}
