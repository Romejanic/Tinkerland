package com.tinkerland.lighting;

import org.joml.Vector3f;

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
	
	public enum LightType {
		
		OMNI(100), SPOT(100), DIRECTIONAL(10);
		
		public final int deferredLimit;
		LightType(int deferredLimit) {
			this.deferredLimit = deferredLimit;
		}
		
	}
	
}