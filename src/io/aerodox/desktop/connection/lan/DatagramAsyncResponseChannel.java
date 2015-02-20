/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.connection.AsyncResponseChannel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author maeglin89273
 *
 */
public class DatagramAsyncResponseChannel extends AsyncResponseChannel {
	private DatagramSocket socket;
	/**
	 * 
	 */
	public DatagramAsyncResponseChannel(DatagramSocket socket) {
		this.socket = socket;
	}

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.AsyncResponseChannel#write(java.lang.String)
	 */
	@Override
	protected void write(String jsonLiteral) throws IOException {
		byte[] buffer = jsonLiteral.getBytes();
		socket.send(new DatagramPacket(buffer, buffer.length));
	}

}
