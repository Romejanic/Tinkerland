package com.tinkerland.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.google.gson.JsonObject;
import com.tinkerland.utils.Mathf;

public class SceneObject {

	public String name = "Scene Object";
	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f();
	public final Vector3f scale    = new Vector3f(1f);
	
	public void render() {}
	
	public boolean canRender() {
		return false;
	}
	
	public Matrix4f getTransform(Matrix4f dest) {
		return dest.identity()
		.translate(this.position)
		.rotateXYZ(Mathf.rad(this.rotation))
		.scale(this.scale);
	}
	
	public final void serializeObject(JsonObject object) {
		object.addProperty("name", this.name);
		object.add("position", JsonHelper.serializeVector3(this.position));
		object.add("rotation", JsonHelper.serializeVector3(this.rotation));
		object.add("scale", JsonHelper.serializeVector3(this.scale));
		object.addProperty("type", getClass().toString());
		serializeProperties(object);
	}
	
	protected void serializeProperties(JsonObject object) {}
	protected void deserializeProperties(JsonObject object) {}
	
	@SuppressWarnings("unchecked")
	public static SceneObject deserializeObject(JsonObject object) {
		if(!object.has("type")) {
			throw new IllegalArgumentException("Given JsonObject is not a SceneObject!");
		}
		String type = object.get("type").getAsString();
		type = type.substring("class ".length());
		
		SceneObject sceneObject = null;
		try {
			Class<? extends SceneObject> clazz = (Class<? extends SceneObject>)Class.forName(type);
			sceneObject = clazz.newInstance();
		} catch (ClassNotFoundException e) {
			System.err.println("Cannot find object class " + type + "!");
			e.printStackTrace(System.err);
			return null;
		} catch(Exception e) {
			System.err.println("Failed to create SceneObject instance!");
			System.err.println("Type: " + type);
			e.printStackTrace(System.err);
			return null;
		}
		
		sceneObject.position.set(JsonHelper.deserializeVector3(object.getAsJsonArray("position")));
		sceneObject.rotation.set(JsonHelper.deserializeVector3(object.getAsJsonArray("rotation")));
		sceneObject.scale.set(JsonHelper.deserializeVector3(object.getAsJsonArray("scale")));
		sceneObject.name = object.get("name").getAsString();
		sceneObject.deserializeProperties(object);
		
		return sceneObject;
	}
	
}