/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.WriterAsyncResponseChannel;
import io.aerodox.desktop.translation.Translator;
import io.aerodox.desktop.translation.Translator.Type;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * @author maeglin89273
 *
 */
public class TCPConnectionHandler implements Runnable {
	
	private Socket socket;
	private Translator translator;
	private JsonParser parser;
//	private DelayEstimator estimator;
	public TCPConnectionHandler(Socket socket) {
		this.socket = socket;
//		this.estimator = new DelayEstimator(100, Unit.MS);	
		
		this.translator = Translator.newTranslator(Type.COMMAND);
		this.parser = new JsonParser();
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
			try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
				AsyncResponseChannel channel = new WriterAsyncResponseChannel(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))) {
				reader.beginArray();
				
				for(; !socket.isClosed() && reader.hasNext();) {
					
					translator.asyncTranslate(this.parser.parse(reader).getAsJsonObject(), channel);
					
				}
				
				reader.endArray();
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("this connection is lost. we will create a new one");
			} finally {
				close();
			}
	}
	
	public void close() {
		
		this.translator.stopTranslation();
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
