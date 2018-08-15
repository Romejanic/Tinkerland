package com.tinkerland.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	
}