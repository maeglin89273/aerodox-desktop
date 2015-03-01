/**
 * 
 */
package io.aerodox.desktop.connection.lan;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.ServerConnection;
import io.aerodox.desktop.connection.ConnectionInfo;
import io.aerodox.desktop.connection.ConnectionInfo.ConnectionType;
import io.aerodox.desktop.connection.WriterAsyncResponseChannel;
import io.aerodox.desktop.service.MessagingService;
import io.aerodox.desktop.service.ServiceManager;
import io.aerodox.desktop.translation.Translator;
import io.aerodox.desktop.translation.TranslatorFactory;
import io.aerodox.desktop.translation.TranslatorFactory.Type;

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
public class TCPConnectionHandler implements Runnable, ServerConnection {
	
	private Socket socket;
	private Translator translator;
	private JsonParser parser;
	private ConnectionInfo info;
	private AsyncResponseChannel rspChannel;
//	private DelayEstimator estimator;
	public TCPConnectionHandler(Socket socket) {
		this.socket = socket;
//		this.estimator = new DelayEstimator(100, Unit.MS);	
		
		this.translator = TranslatorFactory.newTranslator(Type.COMMAND);
		this.parser = new JsonParser();
		this.info = new ConnectionInfo(this, true, ConnectionType.LAN, socket.getInetAddress().getHostName());
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.start();
	}
	
	@Override
	public void start() {
		try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			AsyncResponseChannel rspChannel = new WriterAsyncResponseChannel(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))) {
			this.rspChannel = rspChannel;
			
			ServiceManager.message().send("lan", this.info);
			reader.beginArray();
			for(; !socket.isClosed() && reader.hasNext();) {
				translator.asyncTranslate(this.parser.parse(reader).getAsJsonObject(), rspChannel);
			}
			
			reader.endArray();
		} catch (Exception e) {
//				e.printStackTrace();
			System.out.println("lan stream is closed");
		} finally {
			this.close();
			
		}
	}
	
	@Override
	public void close() {
		this.translator.stopTranslation();
		this.translator = null;
		this.rspChannel = null;
		try {
			this.socket.close();
			this.socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.info.setConnected(false);
		ServiceManager.message().send("lan", this.info);
	}
	@Override
	public AsyncResponseChannel getResponseChannel() {
		return this.rspChannel;
	}
	@Override
	public boolean isClosed() {
		if (this.socket == null) {
			return true;
		}
		return this.socket.isClosed();
	}
	
}
