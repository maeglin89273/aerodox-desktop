/**
 * 
 */
package io.aerodox.desktop.prototype;

import io.aerodox.desktop.math.Vector3D;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * @author maeglin89273
 *
 */
public class DesktopPrototype {
	private static final int PORT = 1810;
	private static final double SCALE_COEF = 3;
	
	private Robot robot;
	private ServerSocket socket;
	
	private JsonParser parser;
	private Gson gson;
	
	
	public DesktopPrototype() {
		try {
			this.robot = new Robot();
			this.socket = new ServerSocket(PORT);
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.parser = new JsonParser();
		this.gson = new Gson();
	}
	
	public void start() {
		showIPs();
		listenMovements();
		
	}

	private void listenMovements() {
		
			try {
				for (;!socket.isClosed();) {
					handleMovement(socket.accept());
				}
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		
	}

	private void handleMovement(Socket accept) {
		try {
			JsonObject object = readJson(accept.getInputStream());
			Vector3D vec = toLinearAcc(object.get("acc"));
			moveMouse(vec);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void moveMouse(Vector3D vec) {
		
		Point point = MouseInfo.getPointerInfo().getLocation();
		this.robot.mouseMove(scale(vec.getX()) + (int)point.getX(), scale(vec.getZ()) + (int)point.getY());
	}

	private int scale(double value) {
		return (int)Math.round(value * SCALE_COEF);
	}

	private JsonObject readJson(InputStream stream) throws IOException {
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
		
		JsonObject object = this.parser.parse(reader).getAsJsonObject();
		reader.close();
		return object;
	}
	
	private Vector3D toLinearAcc(JsonElement element) {
		return this.gson.fromJson(element, Vector3D.class);
	}
	
	private void showIPs() {
		List<String> ips = getActiveIPs();
		System.out.println("The host ip is:");
		for (String ip: ips) {
			System.out.println(ip);
		}
	}

	private List<String> getActiveIPs() {
		List<String> result = new LinkedList<String>();  
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface netInterface;
			for (; netInterfaces.hasMoreElements();) {
				netInterface = netInterfaces.nextElement();
				if (!netInterface.isLoopback() && netInterface.isUp()) {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					InetAddress address;
					for (; addresses.hasMoreElements();) {
						 address = addresses.nextElement();
						if (address instanceof Inet4Address) {
							result.add(address.getHostAddress());
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
