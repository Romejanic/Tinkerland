package com.tinkerland.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.tinkerland.utils.Mathf;
import com.tinkerland.utils.Temp;

public abstract class Camera implements IView {

	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f();

	public float fov  = 70f;
	public float near = 0.1f;
	public float far  = 1000f;
	
	private Matrix4f projMat = new Matrix4f();
	private Matrix4f viewMat = new Matrix4f();
	private Matrix4f  camMat = new Matrix4f();
	private Vector3f forward = new Vector3f();

	private boolean forwardSet = false;

	public Camera(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	protected abstract void moveCamera();

	public void update(int w, int h) {
		this.forwardSet = false;
		moveCamera();
		if(!this.forwardSet) {
			calculateForward();
		}
		
		Vector3f angles = Mathf.rad(this.rotation);
		this.viewMat.identity().rotateXYZ(angles).translate(this.position.negate(Temp.VEC3));
		this.projMat.setPerspective(Mathf.rad(this.fov), (float)w/(float)h, this.near, this.far);
		this.projMat.mul(this.viewMat, this.camMat);
	}

	protected void calculateForward() {
		Mathf.rotationToVector(Mathf.rad(this.rotation), this.forward);
		this.forwardSet = true;
	}

	public Vector3f getForward() {
		return this.forward;
	}
	
	@Override
	public Vector3f getPosition() {
		return this.position;
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return this.projMat;
	}

	@Override
	public Matrix4f getViewMatrix() {
		return this.viewMat;
	}

	@Override
	public Matrix4f getCameraMatrix() {
		return this.camMat;
	}

}
