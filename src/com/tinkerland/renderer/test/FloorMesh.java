package com.tinkerland.renderer.test;

public class FloorMesh extends MeshTest {

	public FloorMesh() {
		super(false);
	}
	
	public float[] getVertexData(boolean isSimple) {
		return new float[] {
			-10f, -0.5f, -10f,    0f, 10f,   0f, 1f, 0f,
			-10f, -0.5f,  10f,   10f, 10f,   0f, 1f, 0f,
			 10f, -0.5f, -10f,    0f, 0f,    0f, 1f, 0f,
			 
			 10f, -0.5f, -10f,    0f, 0f,    0f, 1f, 0f,
			-10f, -0.5f,  10f,   10f, 10f,   0f, 1f, 0f,
			 10f, -0.5f,  10f,    10f, 0f,   0f, 1f, 0f
		};
	}
	
}