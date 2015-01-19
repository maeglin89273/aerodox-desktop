/**
 * 
 */
package io.aerodox.desktop.test;

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
	 */
	public static void main(String[] args) {
//		Vector3D v = new Vector3D(1, 1, 1);
//		Vector2D proj = v.projectToPlane(new Vector3D(500, 1, 500), new Vector3D(-1, 0, 0), new Vector3D(0, 0, -1));
//		System.out.println(proj.getX() + " " + proj.getY());
		
		String arrayS = "[1, 2, 3, 4]";
		JsonParser parser = new JsonParser();
		
		System.out.println(new Gson().fromJson(parser.parse(arrayS).getAsJsonArray(), double[].class));
	}

}
