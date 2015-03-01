/**
 * 
 */
package io.aerodox.desktop.connection.bluettooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.ConnectionInfo;
import io.aerodox.desktop.connection.ConnectionInfo.ConnectionType;
import io.aerodox.desktop.connection.ServerConnection;
import io.aerodox.desktop.connection.WriterAsyncResponseChannel;
import io.aerodox.desktop.service.ServiceManager;
import io.aerodox.desktop.test.DelayEstimator;
import io.aerodox.desktop.test.DelayEstimator.Unit;
import io.aerodox.desktop.translation.Translator;
import io.aerodox.desktop.translation.TranslatorFactory;
import io.aerodox.desktop.translation.TranslatorFactory.Type;

/**
 * @author maeglin89273
 *
 */
public class BluetoothConnectionHandler implements ServerConnection {
	
	private StreamConnection socket;
	private Translator translator;
	private JsonParser parser;
	private ConnectionInfo info;
	private AsyncResponseChannel rspChannel;
	private DelayEstimator estimator;
	
	BluetoothConnectionHandler(StreamConnection socket) {
		this.socket = socket;
		this.translator = TranslatorFactory.newTranslator(Type.FULL);
		this.parser = new JsonParser();
		this.estimator = new DelayEstimator(10, Unit.MS);
		try {
			RemoteDevice mobile = RemoteDevice.getRemoteDevice(socket);
			this.info = new ConnectionInfo(this, true, ConnectionType.BLUETOOTH, mobile.getFriendlyName(true));
			ServiceManager.message().send("bluetooth", this.info);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.ServerConnection#start()
	 */
	@Override
	public void start() {
		try(JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.openInputStream())));
			AsyncResponseChannel rspChannel = new WriterAsyncResponseChannel(new BufferedWriter(new OutputStreamWriter(socket.openOutputStream())))) {
			this.rspChannel = rspChannel;
			
			reader.beginArray();
			for(; reader.hasNext();) {
				translator.asyncTranslate(this.parser.parse(reader).getAsJsonObject(), rspChannel);
//				estimator.estimate();
			}
			
			reader.endArray();
		} catch (IOException e) {
			System.out.println("bluetooth stream is closed");
		} finally {
			this.close();
		}
	}

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.ServerConnection#getResponseChannel()
	 */
	@Override
	public AsyncResponseChannel getResponseChannel() {
		return this.rspChannel;
	}

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.ServerConnection#close()
	 */
	@Override
	public void close() {
		translator.stopTranslation();
		translator = null;
		this.rspChannel = null;
		try {
			this.socket.close();
			this.socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.info.setConnected(false);
		ServiceManager.message().send("bluetooth", this.info);
	}

	@Override
	public boolean isClosed() {
		return this.socket == null;
	}
	
}
