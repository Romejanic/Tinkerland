package com.tinkerland.renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.tinkerland.input.Input;

public class Window {

	private static long window  = NULL;
	private static int width    = 1024;
	private static int height   = 640;
	private static String title = "GLFW Window";
	
	public static void create() throws Exception {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!glfwInit()) {
			throw new RuntimeException("GLFW failed to initialize!");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if(window == NULL) {
			throw new RuntimeException("Failed to create window!");
		}
		glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
			width = w; height = h;
		});
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		MemoryStack.stackPush();
		MemoryStack.stackPush();
		IntBuffer wb = MemoryStack.stackMallocInt(1);
		IntBuffer hb = MemoryStack.stackMallocInt(1);
		glfwGetFramebufferSize(window, wb, hb);
		width  = wb.get();
		height = hb.get();
		MemoryStack.stackPop();
		MemoryStack.stackPop();
		
		Input.setHandle(window);
	}
	
	public static void update() {
		Input.update();
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public static void destroy() {
		if(window != NULL) {
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
		}
		glfwTerminate();
	}
	
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public static String getTitle() {
		return title;
	}
	
	public static void setTitle(String title) {
		Window.title = title;
		if(window != NULL) {
			glfwSetWindowTitle(window, title);
		}
	}
	
}