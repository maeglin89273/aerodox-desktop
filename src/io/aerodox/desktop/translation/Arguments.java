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
	
	public Vector3D asVector3D(String argName) {
		return this.gson.fromJson(this.jsonArgs.get(argName), Vector3D.class);
	}
	
	public MouseButtonState asMouseButton(String argName) {
		return this.gson.fromJson(this.jsonArgs.get(argName), MouseButtonState.class);
	}
}