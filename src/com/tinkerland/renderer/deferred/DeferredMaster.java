package com.tinkerland.renderer.deferred;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import com.tinkerland.camera.IView;
import com.tinkerland.renderer.obj.Mesh;
import com.tinkerland.renderer.obj.Mesher;
import com.tinkerland.renderer.obj.Shader;
import com.tinkerland.renderer.test.DeferredTest;
import com.tinkerland.scene.Scene;
import com.tinkerland.scene.SceneObject;
import com.tinkerland.utils.Temp;

public class DeferredMaster {
	
	private final DeferredFBO fbo;
	private final AccumPass accum;
	private final Mesh quad;
	private final Shader combineShader;
	
	private final DeferredTest test;
	
	private Shader deferredPassShader;
	
	public DeferredMaster() {
		this.fbo   = new DeferredFBO();
		this.accum = new AccumPass();
		this.quad  = Mesher.getFullscreenQuad();
		this.combineShader = Shader.get("deferred/combine");
		
		this.test = new DeferredTest(this.fbo, this.accum);
		
		this.deferredPassShader = Shader.get("deferred/pass");
		this.deferredPassShader.uniform("albedoTex").set(0);
		
		this.combineShader.uniform("gBuffer").set(0);
		this.combineShader.uniform("diffuse").set(1);
		this.combineShader.uniform("specular").set(2);
	}
	
	public void renderDeferred(Scene scene, IView camera, int w, int h) {
		glClearColor(0f, 0f, 0f, 0f);
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE0);
		this.fbo.bind(w, h);
		this.deferredPassShader.bind();
		this.deferredPassShader.uniform("projViewMat").set(camera.getCameraMatrix());
		for(SceneObject object : scene.getSceneObjects()) {
			if(!object.canRender()) {
				continue;
			}
			this.deferredPassShader.uniform("modelMat").set(object.getTransform(Temp.MAT4));
			object.render();
		}
		this.deferredPassShader.unbind();
		this.fbo.unbind();
		
		glDisable(GL_DEPTH_TEST);
		this.accum.drawPass(w, h, camera, scene, this.fbo);
		
		glViewport(0, 0, w, h);
		glClear(GL_COLOR_BUFFER_BIT);
//		this.test.draw();
		this.fbo.bindGBuffer(0);
		this.accum.bindDiffuse(1);
		this.accum.bindSpecular(2);
		this.combineShader.bind();
		this.combineShader.uniform("ambient").set(scene.ambientColor);
		this.quad.bindAndDraw();
		this.combineShader.unbind();
	}
	
	public void delete() {
		this.fbo.delete();
		this.accum.delete();
		this.test.delete();
	}
	
}