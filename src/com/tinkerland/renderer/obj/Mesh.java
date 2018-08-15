package com.tinkerland.renderer.obj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

	private int vao;
	private int elementCount;
	private int[] attribs;
	
	protected Mesh(int vao, int elementCount, int... attribs) {
		this.vao = vao;
		this.elementCount = elementCount;
		this.attribs = attribs;
	}
	
	public void bind() {
		glBindVertexArray(this.vao);
		for(int attrib : attribs) {
			glEnableVertexAttribArray(attrib);
		}
	}
	
	public void unbind() {
		for(int attrib : attribs) {
			glDisableVertexAttribArray(attrib);
		}
		glBindVertexArray(0);
	}
	
	public void draw() {
		glDrawArrays(GL_TRIANGLES, 0, this.elementCount);
	}
	
	public void bindAndDraw() {
		bind();
		draw();
		unbind();
	}
	
}