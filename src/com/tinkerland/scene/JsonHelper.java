package com.tinkerland.scene;

import org.joml.Vector3f;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.tinkerland.utils.Color;

public class JsonHelper {

	public static JsonArray serializeColor(Color color) {
		JsonArray array = new JsonArray();
		array.add(new JsonPrimitive(color.r));
		array.add(new JsonPrimitive(color.g));
		array.add(new JsonPrimitive(color.b));
		if(color.a != 1f) {
			array.add(new JsonPrimitive(color.a));
		}
		return array;
	}
	
	public static Color deserializeColor(JsonArray array) {
		if(array.size() < 3 || array.size() > 4) {
			throw new IllegalArgumentException("Color array length is not 3 or 4!");
		}
		float r = array.get(0).getAsFloat();
		float g = array.get(1).getAsFloat();
		float b = array.get(2).getAsFloat();
		float a = 1f;
		if(array.size() == 4) {
			a = array.get(3).getAsFloat();
		}
		return new Color(r, g, b, a);
	}
	
	public static JsonArray serializeVector3(Vector3f vector) {
		JsonArray array = new JsonArray();
		array.add(new JsonPrimitive(vector.x));
		array.add(new JsonPrimitive(vector.y));
		array.add(new JsonPrimitive(vector.z));
		return array;
	}
	
	public static Vector3f deserializeVector3(JsonArray array) {
		if(array.size() != 3) {
			throw new IllegalArgumentException("Vector array length is not 3!");
		}
		float x = array.get(0).getAsFloat();
		float y = array.get(1).getAsFloat();
		float z = array.get(2).getAsFloat();
		return new Vector3f(x, y, z);
	}
	
}