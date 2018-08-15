package com.tinkerland.renderer;

import static org.lwjgl.opengl.GL11.*;

import com.tinkerland.camera.IView;
import com.tinkerland.renderer.deferred.DeferredMaster;
import com.tinkerland.renderer.obj.Mesher;
import com.tinkerland.renderer.obj.Shader;
import com.tinkerland.renderer.obj.Texture;
import com.tinkerland.scene.Scene;

public class MasterRenderer {

	private DeferredMaster deferred;
	
	public void init() throws Exception {
		glClearColor(0f, 0f, 0f, 1f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glDepthFunc(GL_LEQUAL);
		glCullFace(GL_BACK);
		
		this.deferred = new DeferredMaster();
	}
	
	public void render(IView camera, Scene scene, int w, int h) {
		this.deferred.renderDeferred(scene, camera, w, h);
	}
	
	public void destroy() {
		Mesher.deleteAll();
		Shader.deleteAll();
		Texture.deleteAll();
		this.deferred.delete();
	}
	
}