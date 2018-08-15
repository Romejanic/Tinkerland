package com.tinkerland.renderer.test;

import com.tinkerland.input.Input;
import com.tinkerland.lighting.Light;
import com.tinkerland.lighting.Light.LightType;
import com.tinkerland.renderer.deferred.AccumPass;
import com.tinkerland.renderer.deferred.DeferredFBO;
import com.tinkerland.renderer.obj.Mesh;
import com.tinkerland.renderer.obj.Mesher;
import com.tinkerland.renderer.obj.Shader;
import com.tinkerland.utils.Color;
import com.tinkerland.utils.Mathf;

public class DeferredTest {

	private static final int BUFFER_COUNT = 5;
	
	private final DeferredFBO fbo;
	private final AccumPass pass;
	private final Mesh mesh;
	private final Shader shader;

	public DeferredTest(DeferredFBO fbo, AccumPass pass) {
		this.fbo    = fbo;
		this.pass   = pass;
		this.mesh   = Mesher.getFullscreenQuad();
		this.shader = Shader.get("deferred/debug");
		
		for(int i = 0; i < BUFFER_COUNT; i++) {
			this.shader.uniform("buffers[" + i + "]").set(i);
		}
	}

	public void draw() {
		int i = (int)(Input.getTime() % (float)BUFFER_COUNT);
		this.shader.uniform("index").set(i);
		
		this.fbo.bindGBuffer(0);
		this.fbo.bindNormals(1);
		this.fbo.bindDepth(2);
		this.pass.bindDiffuse(3);
		this.pass.bindSpecular(4);
		
		this.shader.bind();
		this.mesh.bindAndDraw();
		this.shader.unbind();
	}
	
	public void delete() {}
	
	public static Light createRandomPointLight() {
		Light light = new Light(LightType.OMNI);
		light.position.x = Mathf.random(-10f, 10f);
		light.position.z = Mathf.random(-10f, 10f);
		light.color      = Color.random();
		return light;
	}

}