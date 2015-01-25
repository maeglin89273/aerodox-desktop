package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.MouseButtonState;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

class Arguments {
	private static final double EXPO = 10000000.0;
	
	private final Gson gson;
	private final JsonObject jsonArgs;
	Arguments(JsonObject jsonArgs) {
		 this.jsonArgs = jsonArgs;
		this.gson  = new Gson();
	}
	
	public Vector3D getAsVector3D(String argName) {
		double[] vector = decompressVec(this.gson.fromJson(this.jsonArgs.get(argName), String[].class));
		return new Vector3D(vector[0], vector[1], vector[2]);
	}
	
	private double[] decompressVec(String[] compressedVec) {
		double[] vector = new double[compressedVec.length];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = Long.valueOf(compressedVec[i], 36) / EXPO;
		}
		
		return vector;
	}
	
	public Vector2D getAsVector2D(String argName) {
		double[] vector = decompressVec(this.gson.fromJson(this.jsonArgs.get(argName), String[].class));
		return new Vector2D(vector[0], vector[1]);
	}
	
	public double[] getAsDoubleArray(String argName) {
		return this.gson.fromJson(this.jsonArgs.get(argName), double[].class);
	}
	
	public MouseButtonState getAsMouseButton(String argName) {
		return this.gson.fromJson(this.jsonArgs.get(argName), MouseButtonState.class);
	}
	
	public JsonObject getRaw() {
		return this.jsonArgs;
	}
}