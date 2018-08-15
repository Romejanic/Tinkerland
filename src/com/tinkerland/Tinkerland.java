package com.tinkerland;

import com.tinkerland.camera.Camera;
import com.tinkerland.camera.FreeCamera;
import com.tinkerland.input.Input;
import com.tinkerland.lighting.Light;
import com.tinkerland.lighting.Light.LightType;
import com.tinkerland.renderer.MasterRenderer;
import com.tinkerland.renderer.Window;
import com.tinkerland.renderer.test.DeferredTest;
import com.tinkerland.renderer.test.FloorMesh;
import com.tinkerland.renderer.test.MeshTest;
import com.tinkerland.renderer.test.RendererTest;
import com.tinkerland.scene.Scene;

public class Tinkerland {

	public static final Tinkerland instance = new Tinkerland();

	public final Camera camera = new FreeCamera(0f, 0f, 1f);
	public final Scene  scene  = new Scene();
	
	public final MasterRenderer renderer = new MasterRenderer();
	
	private void init() throws Throwable {
		Window.setTitle("Tinkerland");
		Window.create();
		
		this.renderer.init();
		
		this.scene.addObject(new RendererTest("tri", new MeshTest()).setTexture("test/a"));
		this.scene.addObject(new RendererTest("floor", new FloorMesh()).setTexture("test/b"));
		
		Light sun = this.scene.addLight(LightType.DIRECTIONAL);
		sun.rotation.set(45f, 45f, 0f);
		for(int i = 0; i < 10; i++) {
			this.scene.addLight(DeferredTest.createRandomPointLight());
		}
//		Light p1 = this.scene.addLight(LightType.OMNI);
//		p1.position.set(-5f, 1f, -5f);
//		p1.color = Color.RED;
//		Light p2 = this.scene.addLight(LightType.OMNI);
//		p2.position.set(-5f, 1f, 0f);
//		p2.color = Color.GREEN;
//		Light p3 = this.scene.addLight(LightType.OMNI);
//		p3.position.set(-5f, 1f, 5f);
//		p3.color = Color.BLUE;
		
		Input.setCursorLocked(true);
	}

	private void update() {
		this.scene.getObjectByName("tri").rotation.y += Input.getDelta() * 45f;
		
		this.camera.update(Window.getWidth(), Window.getHeight());
		this.renderer.render(this.camera, this.scene, Window.getWidth(), Window.getHeight());
		Window.update();
	}

	private void destroy() {
		this.renderer.destroy();
		Window.destroy();
	}

	public static void main(String[] args) {
		try {
			instance.init();
			while(!Window.shouldClose()) {
				instance.update();
			}
			instance.destroy();
		} catch(Throwable e) {
			System.err.println("!! CRASHED !!");
			System.err.print("Caused by ");
			e.printStackTrace(System.err);
			instance.destroy();
		}
	}

}