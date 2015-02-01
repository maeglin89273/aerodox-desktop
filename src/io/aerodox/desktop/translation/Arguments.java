package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.MouseButtonState;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class Arguments {
	
	private static final Gson GSON = new Gson();
	private final JsonObject jsonArgs;
	
	Arguments(JsonObject jsonArgs) {
		 this.jsonArgs = jsonArgs;
	}
	
	public Vector3D getAsVector3D(String argName) {
		return toVector3D(this.jsonArgs.get(argName));
	}
	
	public Vector2D getAsVector2D(String argName) {
		return toVector2D(this.jsonArgs.get(argName));
	}
	
	public double[] getAsDoubleArray(String argName) {
		return toDoubleArray(this.jsonArgs.get(argName));
	}
	
	public MouseButtonState getAsMouseButton(String argName) {
		return GSON.fromJson(this.jsonArgs.get(argName), MouseButtonState.class);
	}
	
	public static Vector3D toVector3D(JsonElement json) {
		double[] vector = DecompressUtility.decompressVector(GSON.fromJson(json, String[].class));
		return new Vector3D(vector[0], vector[1], vector[2]);
	}
	
	public Vector2D toVector2D(JsonElement json) {
		double[] vector = DecompressUtility.decompressVector(GSON.fromJson(json, String[].class));
		return new Vector2D(vector[0], vector[1]);
	}
	
	public double[] toDoubleArray(JsonElement element) {
		return GSON.fromJson(element, double[].class);
	}
	
	public JsonObject getRaw() {
		return this.jsonArgs;
	}
	
	private static class DecompressUtility {
		private static final double EXPO = 10000000.0;
		
		public static double[] decompressVector(String[] compressedVec) {
			double[] vector = new double[compressedVec.length];
			for (int i = 0; i < vector.length; i++) {
				vector[i] = Long.valueOf(compressedVec[i], 36) / EXPO;
			}
			
			return vector;
		}
	}
}