package com.tinkerland.renderer.test;

import com.tinkerland.renderer.obj.Texture;
import com.tinkerland.scene.SceneObject;

public class RendererTest extends SceneObject {

	private MeshTest mesh;
	
	public RendererTest(String name, MeshTest mesh) {
		super();
		this.name = name;
		this.mesh = mesh;
	}
	
	public RendererTest setTexture(String texture) {
		this.mesh.setTexture(Texture.get(texture));
		return this;
	}
	
	@Override
	public void render() {
		this.mesh.draw();
	}
	
	@Override
	public boolean canRender() {
		return true;
	}
	
}
