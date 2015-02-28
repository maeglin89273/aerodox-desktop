/**
 * 
 */
package io.aerodox.desktop.test;

import java.io.DataInputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @author maeglin89273
 *
 */
public class BluetoothTest {
	
    public final UUID uuid = new UUID(                             //the uid of the service, it has to be unique,
			"27012f0c68af4fbf8dbe6bbaf7aa432a", false); //it can be generated randomly
    public final String name = "Echo Server";                       //the name of the service
    public final String url  =  "btspp://localhost:" + uuid         //the service url
                                + ";name=" + name 
                                + ";authenticate=false;encrypt=false;";
    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;

    public BluetoothTest() {
        try {
            System.out.println("Setting device to be discoverable...");
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            System.out.println("Start advertising service...");
            server = (StreamConnectionNotifier)Connector.open(url);
            System.out.println("Waiting for incoming connection...");
            conn = server.acceptAndOpen();
            System.out.println("Client Connected...");
            DataInputStream din   = new DataInputStream(conn.openInputStream());
            while(true){
                String cmd = "";
                char c;
                while (((c = din.readChar()) > 0) && (c!='\n') ){
                    cmd = cmd + c;
                }
                System.out.println("Received " + cmd);
            }
            
        } catch (Exception  e) {System.out.println("Exception Occured: " + e.toString());}
    }
    
    public static void main (String args[]){
//        BluetoothTest echoserver = new BluetoothTest(); 
    	try {
			System.out.println(LocalDevice.getLocalDevice().getFriendlyName());
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	    
	

}
