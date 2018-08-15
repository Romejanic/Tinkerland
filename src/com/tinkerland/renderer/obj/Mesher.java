package com.tinkerland.renderer.obj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.tinkerland.utils.Buffers;

public class Mesher {
	
	private static final ArrayList<Integer> vaos = new ArrayList<Integer>();
	private static final ArrayList<Integer> vbos = new ArrayList<Integer>();
	private static final HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();
	
	private static Mesh getMesh(String mesh) {
		if(meshes.containsKey(mesh)) {
			return meshes.get(mesh);
		}
		return null;
	}
	
	public static Mesh getFullscreenQuad() {
		Mesh mesh = getMesh("fullscreen_quad");
		if(mesh != null) {
			return mesh;
		}
		
		float[] vertices = {
			-1f, -1f,
			 1f, -1f,
			-1f,  1f,
			
			-1f,  1f,
			 1f, -1f,
			 1f,  1f
		};
		
		int vao = glGenVertexArrays();
		int vbo = glGenBuffers();
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Buffers.fromArray(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		mesh = new Mesh(vao, vertices.length / 2, 0);
		
		vaos.add(vao);
		vbos.add(vbo);
		meshes.put("fullscreen_quad", mesh);
		
		return mesh;
	}
	
	public static void deleteAll() {
		glDeleteVertexArrays(Buffers.fromIntList(vaos));
		glDeleteBuffers(Buffers.fromIntList(vbos));
		vaos.clear(); vbos.clear(); meshes.clear();
	}
	
}