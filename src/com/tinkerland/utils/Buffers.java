package com.tinkerland.utils;

import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.BufferUtils.createIntBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class Buffers {

	public static FloatBuffer fromArray(float... array) {
		return (FloatBuffer)createFloatBuffer(array.length).put(array).flip();
	}
	
	public static IntBuffer fromArray(int... array) {
		return (IntBuffer)createIntBuffer(array.length).put(array).flip();
	}
	
	public static IntBuffer fromIntList(ArrayList<Integer> list) {
		IntBuffer buf = BufferUtils.createIntBuffer(list.size());
		for(int i = 0; i < list.size(); i++) {
			buf.put(list.get(i));
		}
		return (IntBuffer)buf.flip();
	}
	
}