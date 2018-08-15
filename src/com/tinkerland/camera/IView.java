package com.tinkerland.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface IView {

	/**
	 * The world space position of the camera.
	 */
	Vector3f getPosition();
	
	/**
	 * The camera's projection (eye -> viewport) matrix.
	 */
	Matrix4f getProjectionMatrix();
	
	/**
	 * The camera's view (world -> eye) matrix.
	 */
	Matrix4f getViewMatrix();
	
	/**
	 * The camera's combined matrix (world -> viewport). Should always be V * P.
	 */
	Matrix4f getCameraMatrix();
	
}