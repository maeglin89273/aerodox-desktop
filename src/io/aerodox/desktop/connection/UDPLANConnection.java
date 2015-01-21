/**
 * 
 */
package io.aerodox.desktop.connection;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.translation.Translator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.google.gson.JsonParser;

/**
 * @author maeglin89273
 *
 */
public class UDPLANConnection extends LANConnection {
	
	private static final int BUFFER_SIZE = 1 << 9;
	
	private DatagramSocket delegate;
	private byte[] buffer;
	private Translator translator;
	private JsonParser parser;
	
	public UDPLANConnection() {
		try {
			this.delegate = new DatagramSocket(AerodoxConfig.PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.buffer = new byte[BUFFER_SIZE];
		this.translator = Translator.newTranslator();
		this.parser = new JsonParser();
	}
	
	@Override
	public void start() {
		this.showIPs();
		recieveDatagram();
	}

	private void recieveDatagram() {
		DatagramPacket actionPacket;
		try {
			for (;!delegate.isClosed();) {
				long time = System.nanoTime();
				actionPacket = new DatagramPacket(this.buffer, this.buffer.length);
				delegate.receive(actionPacket);
				long diff = System.nanoTime() - time;
				if (diff > 500000000) {
					System.out.printf("delay about %.2fms\n", diff / 1000000.0);
				}
				handlePacket(actionPacket);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	private void handlePacket(DatagramPacket packet) {
		
		String jsonLiteral = new String(packet.getData(), 0, packet.getLength());
//		System.out.println(this.parser.parse(jsonLiteral).getAsJsonObject().toString());
		translator.asyncTranslate(this.parser.parse(jsonLiteral).getAsJsonObject(), null);
		
	}
	
	@Override
	public void close() {
		this.delegate.close();
	}

}
