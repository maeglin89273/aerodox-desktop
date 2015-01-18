package io.aerodox.desktop.imitation;
import java.awt.event.InputEvent;

/**
 * 
 */

/**
 * @author maeglin89273
 *
 */
public class MouseButtonState {
	private boolean isPress;
	
	private int num;
	
	
	public MouseButtonState(int num, boolean press) {
		this.num = num;
		this.isPress = press;
	}
	
	public int getMask() {
		return InputEvent.getMaskForButton(num);
	}

	public boolean isPress() {
		return this.isPress;
	}
	
}
