package com.tinkerland.renderer.test;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.tinkerland.renderer.obj.Texture;
import com.tinkerland.utils.Buffers;

public class MeshTest {

	private int vao;
	private int vbo;
	private int elementCount;
	private boolean simple;

	private Texture texture;

	public MeshTest() {
		this(false);
	}

	public MeshTest(boolean simple) {
		this.simple = simple;
		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();

		float[] vertices  = getVertexData(simple);
		int dataPerVertex = simple ? 3 : 8;
		this.elementCount = vertices.length / dataPerVertex;

		glBindVertexArray(this.vao);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, Buffers.fromArray(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, dataPerVertex * 4, 0);
		if(!simple) {
			glVertexAttribPointer(1, 2, GL_FLOAT, false, dataPerVertex * 4, 3 * 4);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, dataPerVertex * 4, 5 * 4);
		}
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);	
	}

	public void draw() {
		if(this.texture != null) {
			this.texture.bind();
		}
		glBindVertexArray(this.vao);
		glEnableVertexAttribArray(0);
		if(!this.simple) {
			glEnableVertexAttribArray(1);
			glEnableVertexAttribArray(2);
		}
		glDrawArrays(GL_TRIANGLES, 0, this.elementCount);
		glDisableVertexAttribArray(0);
		if(!this.simple) {
			glDisableVertexAttribArray(1);
			glDisableVertexAttribArray(2);
		}
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteVertexArrays(this.vao);
		glDeleteBuffers(this.vbo);
	}

	public Texture getTexture() {
		return this.texture;
	}

	public MeshTest setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public float[] getVertexData(boolean isSimple) {
		if(isSimple) {
			return new float[] {
					-0.5f, -0.5f, 0.0f,
					0.5f, -0.5f, 0.0f,
					0.0f,  0.5f, 0.0f,
					
					0.0f, 0.5f, 0.0f,
					0.5f, -0.5f, 0.0f,
					-0.5f, -0.5f, 0.0f
			};
		} else {
			return new float[] {
					-0.5f, -0.5f, 0.0f,     0f, 1f,   0f, 0f, 1f,
					0.5f, -0.5f, 0.0f,     1f, 1f,   0f, 0f, 1f,
					0.0f,  0.5f, 0.0f,   0.5f, 0f,   0f, 0f, 1f,
					
					0.0f,  0.5f, 0.0f,   0.5f, 0f,   0f, 0f, -1f,
					0.5f, -0.5f, 0.0f,     1f, 1f,   0f, 0f, -1f,
					-0.5f, -0.5f, 0.0f,     0f, 1f,   0f, 0f, -1f,
			};
		}
	}

}