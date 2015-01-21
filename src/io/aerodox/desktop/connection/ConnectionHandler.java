/**
 * 
 */
package io.aerodox.desktop.connection;

import io.aerodox.desktop.translation.Translator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * @author maeglin89273
 *
 */
public class ConnectionHandler implements Runnable {
	
	private Socket socket;
	private Translator translator;
	private JsonParser parser;
	public ConnectionHandler(Socket socket) {
		this.socket = socket;
		this.translator = Translator.newTranslator();
		this.parser = new JsonParser();
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
			try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
				AsyncResponseChannel channel = new AsyncResponseChannel(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))) {
				reader.beginArray();
				
				for(;reader.hasNext();) {
					
//					System.out.println(obj.toString().getBytes().length);
					translator.asyncTranslate(this.parser.parse(reader).getAsJsonObject(), channel);
				}
				
				reader.endArray();
			} catch (IOException e) {
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
