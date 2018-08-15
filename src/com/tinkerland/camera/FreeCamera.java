package com.tinkerland.camera;

import org.joml.Vector3f;

import com.tinkerland.input.Input;
import com.tinkerland.input.Input.Key;
import com.tinkerland.utils.Mathf;
import com.tinkerland.utils.Temp;

public class FreeCamera extends Camera {

	public float flySpeed  = 5f;
	public float lookSpeed = 25f;
	public float boost     = 3f;
	
	public FreeCamera(float x, float y, float z) {
		super(x, y, z);
	}

	@Override
	protected void moveCamera() {
		float lookMul = this.lookSpeed * Input.getDelta();
		this.rotation.x += lookMul * (float)Input.getMouseDY();
		this.rotation.y += lookMul * (float)Input.getMouseDX();
		this.rotation.x  = Mathf.clamp(-89.9f, 89.9f, this.rotation.x);
		this.rotation.y %= 360f;
		
		this.calculateForward();
		
		float speedMul = this.flySpeed * Input.getDelta();
		if(Input.isKeyDown(Key.LSHIFT)) {
			speedMul *= this.boost;
		}
		
		Vector3f flyVec = this.getForward().mul(speedMul, Temp.VEC3);
		if(Input.isKeyDown(Key.W)) {
			this.position.add(flyVec);
		}
		if(Input.isKeyDown(Key.S)) {
			this.position.sub(flyVec);
		}
		
		float sinY90 = Mathf.sin(Mathf.rad(this.rotation.y) + Mathf.PI_OVER_2);
		float cosY90 = Mathf.cos(Mathf.rad(this.rotation.y) + Mathf.PI_OVER_2);
		if(Input.isKeyDown(Key.A)) {
			this.position.x -= sinY90 * speedMul;
			this.position.z += cosY90 * speedMul;
		}
		if(Input.isKeyDown(Key.D)) {
			this.position.x += sinY90 * speedMul;
			this.position.z -= cosY90 * speedMul;
		}
	}

}
