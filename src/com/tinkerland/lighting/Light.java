package com.tinkerland.lighting;

import org.joml.Vector3f;

import com.google.gson.JsonObject;
import com.tinkerland.scene.JsonHelper;
import com.tinkerland.utils.Color;

public class Light {

	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f();
	
	public LightType type  = LightType.OMNI;
	public float intensity = 1f;
	public Color color     = Color.WHITE;
	
	public float range     = 10f;
	public float spotAngle = 45f;
	public float spotSoftness = 0.05f;
	
	public Light() {
		this(LightType.OMNI);
	}
	
	public Light(LightType type) {
		this.type = type;
	}
	
	public Light(float x, float y, float z) {
		this(x, y, z, LightType.OMNI);
	}
	
	public Light(float x, float y, float z, LightType type) {
		this(type);
		this.position.set(x, y, z);
	}
	
	public final void serializeLight(JsonObject object) {
		object.add("position", JsonHelper.serializeVector3(this.position));
		object.add("rotation", JsonHelper.serializeVector3(this.rotation));
		object.add("color", JsonHelper.serializeColor(this.color));
		object.addProperty("type", this.type.name());
		object.addProperty("intensity", this.intensity);
		if(this.type == LightType.OMNI || this.type == LightType.SPOT) {
			object.addProperty("range", this.range);
			if(this.type == LightType.SPOT) {
				object.addProperty("spotAngle", this.spotAngle);
				object.addProperty("spotSoftness", this.spotSoftness);
			}
		}
		serializeProperties(object);
	}
	
	protected void serializeProperties(JsonObject object) {}
	protected void deserializeProperties(JsonObject object) {}
	
	public enum LightType {
		
		OMNI(100), SPOT(100), DIRECTIONAL(10);
		
		public final int deferredLimit;
		LightType(int deferredLimit) {
			this.deferredLimit = deferredLimit;
		}
		
	}
	
}