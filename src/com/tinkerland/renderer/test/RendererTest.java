package com.tinkerland.renderer.test;

import com.google.gson.JsonObject;
import com.tinkerland.renderer.obj.Texture;
import com.tinkerland.scene.SceneObject;

public class RendererTest extends SceneObject {

	private TestMeshes mesh;
	
	public RendererTest(String name, TestMeshes mesh) {
		super();
		this.name = name;
		this.mesh = mesh;
	}
	
	public RendererTest() {
		super();
	}
	
	public RendererTest setTexture(String texture) {
		this.mesh.mesh.setTexture(Texture.get(texture));
		return this;
	}
	
	@Override
	public void render() {
		this.mesh.mesh.draw();
	}
	
	@Override
	public boolean canRender() {
		return true;
	}
	
	@Override
	protected void serializeProperties(JsonObject object) {
		object.addProperty("mesh", this.mesh.name());
		object.addProperty("texture", this.mesh.mesh.getTexture().getName());
	}
	
	@Override
	protected void deserializeProperties(JsonObject object) {
		this.mesh = TestMeshes.valueOf(object.get("mesh").getAsString());
		this.setTexture(object.get("texture").getAsString());
	}
	
	public enum TestMeshes {
		
		TRIANGLE(new MeshTest()),
		FLOOR(new FloorMesh());
		
		public final MeshTest mesh;
		
		TestMeshes(MeshTest mesh) {
			this.mesh = mesh;
		}
		
	}
	
}
