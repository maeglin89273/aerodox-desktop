/**
 * 
 */
package io.aerodox.desktop.test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.Deflater;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

/**
 * @author maeglin89273
 *
 */
public class ClassTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		testCompress();
//		System.out.println(Long.toString(235312571257L, 36));
	}
	
	private static void testCompress() {
		String json = "{\"rot\":{\"z\":0.8715385794639587,\"y\":0.05660635977983475,\"x\":-0.011508936993777752},\"action\":\"move\"}";
		System.out.println(json.length());
//		byte[] buffer = new byte[100];
//		byte[] data = json.getBytes();
//		int size = compress(data, buffer);
//		System.out.println(data.length + " " + size);
	}
	
	private static int compress(byte[] data, byte[] buffer) {
		Deflater compresser = new Deflater();
		compresser.setInput(data);
		compresser.finish();
		int size = compresser.deflate(buffer);
		compresser.end();
		return size;
	}
	
	private static void testSize() throws IOException {
		File pojoFile = new File("pojo.dat");
		File jsonFile = new File("json.dat");
		pojoFile.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pojoFile))); 
		BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
		Vector3D vec = new Vector3D(123, 456, 789);
		oos.writeObject(vec);
		oos.close();
		
		writer.write(new Gson().toJson(vec));
		writer.close();
	}

}
