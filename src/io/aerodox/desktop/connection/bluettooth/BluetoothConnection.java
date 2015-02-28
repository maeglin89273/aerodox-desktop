/**
 * 
 */
package io.aerodox.desktop.connection.bluettooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import io.aerodox.desktop.connection.AsyncResponseChannel;
import io.aerodox.desktop.connection.BasicConnection;
import io.aerodox.desktop.connection.ConnectionInfo;
import io.aerodox.desktop.connection.ConnectionInfo.ConnectionType;
import io.aerodox.desktop.connection.lan.HasAddressConnection;
import io.aerodox.desktop.connection.WriterAsyncResponseChannel;
import io.aerodox.desktop.service.ServiceManager;
import io.aerodox.desktop.translation.Translator;
import io.aerodox.desktop.translation.TranslatorFactory;
import io.aerodox.desktop.translation.TranslatorFactory.Type;

/**
 * @author maeglin89273
 *
 */
public class BluetoothConnection extends BasicConnection implements HasAddressConnection {
	private StreamConnectionNotifier delegate;
	private Translator translator;
	private JsonParser parser;
	private ConnectionInfo info;
	private AsyncResponseChannel rspChannel;
	private String address;
	
	public BluetoothConnection() {
		this.translator = TranslatorFactory.newTranslator(Type.FULL);
		this.parser = new JsonParser();
		this.delegate = initBluetoothServer();
	}
	
	private StreamConnectionNotifier initBluetoothServer() {
		StreamConnectionNotifier notifier = null;
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			this.address = localDevice.getBluetoothAddress();
			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			
			notifier = (StreamConnectionNotifier) Connector.open(BluetoothConfig.URL);
		} catch (IOException e) {
			ServiceManager.message().send("info", "The bluetooth is not available. Please turn on your bluetooth and set it to visible");
			this.close();
		}
		return notifier;
	}
	
	@Override
	public String getAddress() {
		return this.address;
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
		
		if (this.delegate != null) {
			this.rspChannel = null;
			try {
				this.delegate.close();
				this.delegate = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.info.setConnected(false);
			ServiceManager.message().send("bluetooth", this.info);
		}
	}

	/* (non-Javadoc)
	 * @see io.aerodox.desktop.connection.BasicConnection#startRecieve()
	 */
	@Override
	protected void startRecieve() {
		if (this.delegate == null) {
			return;
		}
		
		try {
			StreamConnection socket = this.delegate.acceptAndOpen();
		
			try(JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.openInputStream())));
				AsyncResponseChannel rspChannel = new WriterAsyncResponseChannel(new BufferedWriter(new OutputStreamWriter(socket.openOutputStream())))) {
				this.rspChannel = rspChannel;
				
				ServiceManager.message().send("bluetooth", this.info);
				reader.beginArray();
				for(; !reader.hasNext();) {
					translator.asyncTranslate(this.parser.parse(reader).getAsJsonObject(), rspChannel);
				}
			} catch (IOException e) {
				System.out.println("bluetooth stream is closed");
			} 
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			this.close();
		}
				
	}

}
