package io.aerodox.desktop.translation;

import io.aerodox.desktop.imitation.MouseButtonState;
import io.aerodox.desktop.math.Vector3D;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

class Arguments {
	private final Gson gson;
	private final JsonObject jsonArgs;
	Arguments(JsonObject jsonArgs) {
		 this.jsonArgs = jsonArgs;
		this.gson  = new Gson();
	}
	
	public Vector3D getAsVector3D(String argName) {
		return this.gson.fromJson(this.jsonArgs.get(argName), Vector3D.class);
	}
	
	public MouseButtonState getAsMouseButton(String argName) {
		return this.gson.fromJson(this.jsonArgs.get(argName), MouseButtonState.class);
	}
	
	public JsonObject getRaw() {
		return this.jsonArgs;
	}
}