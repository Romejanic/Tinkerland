package com.tinkerland.input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

public class Input {

	private static long handle = NULL;
	private static float lastFrame = 0f;
	private static float delta = 0f;
	
	private static double mx  = 0d;
	private static double my  = 0d;
	private static double mdx = 0d;
	private static double mdy = 0d;
	private static DoubleBuffer mxb = BufferUtils.createDoubleBuffer(1);
	private static DoubleBuffer myb = BufferUtils.createDoubleBuffer(1);
	
	private static boolean[] wasPressed = new boolean[Key.values().length];
	
	public static float getTime() {
		return (float)glfwGetTime();
	}
	
	public static float getDelta() {
		return delta;
	}
	
	public static double getMouseX() {
		return mx;
	}
	
	public static double getMouseY() {
		return my;
	}
	
	public static double getMouseDX() {
		return mdx;
	}
	
	public static double getMouseDY() {
		return mdy;
	}
	
	public static boolean isCursorLocked() {
		return glfwGetInputMode(handle, GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
	}
	
	public static void setCursorLocked(boolean locked) {
		glfwSetInputMode(handle, GLFW_CURSOR, locked ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
	}
	
	public static boolean isKeyDown(Key key) {
		return glfwGetKey(handle, key.code) == GLFW_PRESS;
	}
	
	public static boolean isKeyPressed(Key key) {
		return isKeyDown(key) && !wasPressed[key.ordinal()];
	}
	
	public static boolean isKeyUp(Key key) {
		return !isKeyDown(key) && wasPressed[key.ordinal()];
	}
	
	public static void update() {
		// calculate delta
		float time = getTime();
		delta = time - lastFrame;
		lastFrame = time;
		
		// update key states
		for(Key key : Key.values()) {
			wasPressed[key.ordinal()] = isKeyDown(key);
		}
		
		// update mouse pos + direction
		mxb.clear(); myb.clear();
		glfwGetCursorPos(handle, mxb, myb);
		mdx = mxb.get(0) - mx;
		mdy = myb.get(0) - my;
		mx  = mxb.get(0);
		my  = myb.get(0);
	}
	
	public static void setHandle(long handle) {
		if(Input.handle != NULL) {
			throw new RuntimeException("Input handle already set!");
		}
		Input.handle = handle;
	}
	
	public enum Key {
		
		W(GLFW_KEY_W),
		A(GLFW_KEY_A),
		S(GLFW_KEY_S),
		D(GLFW_KEY_D),
		LSHIFT(GLFW_KEY_LEFT_SHIFT);
		
		protected final int code;
		
		Key(int code) {
			this.code = code;
		}
		
	}
	
}