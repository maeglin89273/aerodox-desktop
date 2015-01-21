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
//		Vector3D v = new Vector3D(1, 1, 1);
//		Vector2D proj = v.projectToPlane(new Vector3D(500, 1, 500), new Vector3D(-1, 0, 0), new Vector3D(0, 0, -1));
//		System.out.println(proj.getX() + " " + proj.getY());
		testSize();
//		"123".getBytes();
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
