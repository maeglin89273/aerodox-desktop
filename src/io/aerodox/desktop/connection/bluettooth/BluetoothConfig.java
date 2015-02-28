/**
 * 
 */
package io.aerodox.desktop.connection.bluettooth;

import javax.bluetooth.UUID;

/**
 * @author maeglin89273
 *
 */
public class BluetoothConfig {
	static final UUID AERODOX_UUID = new UUID("43432e4c69616f3c3357432e48736961", false);
	static final String URL = "btspp://localhost:" + AERODOX_UUID.toString() + ";name=aerodox";
}
