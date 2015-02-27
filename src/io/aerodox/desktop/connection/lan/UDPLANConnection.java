/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.AerodoxConfig;
import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.BasicConnection;
import io.aerodox.desktop.connection.ServerConnection;
import io.aerodox.desktop.test.DelayEstimator;
import io.aerodox.desktop.test.DelayEstimator.Unit;
import io.aerodox.desktop.translation.Translator;
import io.aerodox.desktop.translation.TranslatorFactory;
import io.aerodox.desktop.translation.TranslatorFactory.Type;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.google.gson.JsonParser;

/**
 * @author maeglin89273
 *
 */
public class UDPLANConnection extends BasicConnection {
	
	private static final int BUFFER_SIZE = 1 << 8;
	
	private DatagramSocket delegate;
	private byte[] buffer;
	private Translator translator;
	private JsonParser parser;
	private AsyncResponseChannel rspChannel;
	private DelayEstimator delay;
	
	public UDPLANConnection() {
		try {
			this.delegate = new DatagramSocket(AerodoxConfig.UDP_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.buffer = new byte[BUFFER_SIZE];
		this.translator = TranslatorFactory.newTranslator(Type.MOTION);
		this.parser = new JsonParser();
		this.delay = new DelayEstimator(200, Unit.MS);
		
	}

	@Override
	protected void startRecieve() {
		DatagramPacket actionPacket;
		try (AsyncResponseChannel rspChannel = new DatagramAsyncResponseChannel(this.delegate)) {
			this.rspChannel = rspChannel;
			for (;!delegate.isClosed();) {
//				delay.start();
				actionPacket = new DatagramPacket(this.buffer, this.buffer.length);
				delegate.receive(actionPacket);
//				delay.estimate();
				handlePacket(actionPacket, rspChannel);
				
			}
			
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("error occurs, socket may close");
		} finally {
			this.close();
		}
	}
	
	private void handlePacket(DatagramPacket packet, AsyncResponseChannel channel) {
		String jsonLiteral = new String(packet.getData(), 0, packet.getLength());
		translator.asyncTranslate(this.parser.parse(jsonLiteral).getAsJsonObject(), channel);
	}
	
	@Override
	public AsyncResponseChannel getResponseChannel() {
		return this.rspChannel;
	}
	
	@Override
	public void close() {
		this.translator.stopTranslation();
		this.rspChannel = null;
		this.delegate.close();
	}
	
	
}
