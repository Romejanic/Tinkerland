package com.tinkerland.scene;

import java.util.ArrayList;

import com.tinkerland.lighting.Light;
import com.tinkerland.lighting.Light.LightType;
import com.tinkerland.utils.Color;

public class Scene {

	private final ArrayList<SceneObject> objects = new ArrayList<SceneObject>();
	private final ArrayList<Light> lights = new ArrayList<Light>();
	
	public Color ambientColor = new Color(25, 25, 25);
	
	public SceneObject addObject(SceneObject object) {
		if(this.objects.contains(object)) {
			return object;
		}
		this.objects.add(object);
		return object;
	}
	
	public Light addLight(Light light) {
		if(this.lights.contains(light)) {
			return light;
		}
		this.lights.add(light);
		return light;
	}
	
	public Light addLight(LightType type) {
		return addLight(new Light(type));
	}
	
	public SceneObject getObjectByName(String name) {
		for(SceneObject obj : this.objects) {
			if(obj.name.equals(name)) {
				return obj;
			}
		}
		return null;
	}
	
	public SceneObject[] getSceneObjects() {
		return this.objects.toArray(new SceneObject[0]);
	}
	
	public Light[] getLights() {
		return this.lights.toArray(new Light[0]);
	}
	
}