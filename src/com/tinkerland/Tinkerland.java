package com.tinkerland;

import java.io.File;

import com.tinkerland.camera.Camera;
import com.tinkerland.camera.FreeCamera;
import com.tinkerland.input.Input;
import com.tinkerland.renderer.MasterRenderer;
import com.tinkerland.renderer.Window;
import com.tinkerland.scene.Scene;
import com.tinkerland.scene.SceneObject;
import com.tinkerland.scene.SceneSave;

public class Tinkerland {

	public static final Tinkerland instance = new Tinkerland();

	public final Camera camera = new FreeCamera(0f, 0f, 1f);
	public Scene  scene  = new Scene();
	
	public final MasterRenderer renderer = new MasterRenderer();
	
	private void init() throws Throwable {
		Window.setTitle("Tinkerland");
		Window.create();
		
		this.renderer.init();
		this.scene = SceneSave.loadScene(new File("scenes/scene.tkl"));
		
		Input.setCursorLocked(true);
	}

	private void update() {
		SceneObject obj = this.scene.getObjectByName("tri");
		if(obj != null) {
			obj.rotation.y += Input.getDelta() * 45f;
		}
		
		this.camera.update(Window.getWidth(), Window.getHeight());
		this.renderer.render(this.camera, this.scene, Window.getWidth(), Window.getHeight());
		Window.update();
	}

	private void destroy() {
		if(this.scene != null) {
			SceneSave.saveScene(this.scene, new File("scenes/scene.tkl"));
		}
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