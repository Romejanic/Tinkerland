package com.tinkerland.renderer.obj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import com.tinkerland.resources.Resources;
import com.tinkerland.utils.ArrayUtils;
import com.tinkerland.utils.Color;

public class Shader {

	private static final FloatBuffer TEMP_MAT4 = BufferUtils.createFloatBuffer(16);
	public static final HashMap<String, Shader> shaders = new HashMap<String, Shader>();
	private static final HashMap<String, String> globalDefines = new HashMap<String, String>();
	
	private final HashMap<String, UniformVar> uniforms = new HashMap<String, UniformVar>();
	
	private final String name;
	private int program;
	
	public Shader(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void bind() {
		glUseProgram(this.program);
		this.uploadUniforms();
	}
	
	private void uploadUniforms() {
		int texUnit = 0;
		for(UniformVar var : uniforms.values()) {
			var.upload(texUnit);
			if(var.isTexture()) {
				texUnit++;
			}
		}
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public boolean isActive() {
		return glGetInteger(GL_CURRENT_PROGRAM) == this.program;
	}
	
	public UniformVar uniform(String name) {
		if(this.uniforms.containsKey(name)) {
			return this.uniforms.get(name);
		}
		UniformVar var = new UniformVar(name, glGetUniformLocation(this.program, name));
		if(!var.isValid()) {
			System.err.println("Uniform variable " + name + " not found in program " + this.name);
		}
		uniforms.put(name, var);
		return var;
	}
	
	public static Shader get(String name) {
		if(shaders.containsKey(name)) {
			return shaders.get(name);
		}
		Shader s = new Shader(name);
		int vs = -1, fs = -1;
		try {
			s.program = glCreateProgram();
			vs = get(name + "_vs", GL_VERTEX_SHADER);
			fs = get(name + "_fs", GL_FRAGMENT_SHADER);
			
			glAttachShader(s.program, vs);
			glAttachShader(s.program, fs);
			glLinkProgram(s.program);
			if(glGetProgrami(s.program, GL_LINK_STATUS) == GL_FALSE) {
				String log = glGetProgramInfoLog(s.program, glGetProgrami(s.program, GL_INFO_LOG_LENGTH));
				throw new RuntimeException("Failed to link program " + name + "!\n" + log);
			}
			
			glDetachShader(s.program, vs);
			glDetachShader(s.program, fs);
		} catch(Exception e) {
			System.err.println("Failed to load shader program: " + name + "!");
			e.printStackTrace(System.err);
			if(s.program > 0) {
				glDeleteProgram(s.program);
				s.program = 0;
			}
		} finally {
			if(vs > -1) {
				glDeleteShader(vs);
			}
			if(fs > -1) {
				glDeleteShader(fs);
			}
		}
		shaders.put(name, s);
		return s;
	}
	
	private static int get(String src, int type) throws Exception {
		int s = glCreateShader(type);
		try {
			ArrayList<String> source = Resources.readLines("shaders/" + src + ".glsl");
			glShaderSource(s, ArrayUtils.joinList(source, "\n"));
			glCompileShader(s);
			if(glGetShaderi(s, GL_COMPILE_STATUS) == GL_FALSE) {
				String log = glGetShaderInfoLog(s, glGetShaderi(s, GL_INFO_LOG_LENGTH));
				throw new RuntimeException("Failed to compile " + src + "!\n" + log);
			}
		} catch(Exception e) {
			glDeleteShader(s);
			throw e;
		}
		return s;
	}
	
	public static void define(String key) {
		define(key, null);
	}
	
	public static void define(String key, String value) {
		if(globalDefines.containsKey(key)) {
			globalDefines.remove(key);
		}
		globalDefines.put(key, value);
	}
	
	public static void undefine(String key) {
		if(globalDefines.containsKey(key)) {
			globalDefines.remove(key);
		}
	}
	
	public static void deleteAll() {
		for(Shader shader : shaders.values()) {
			glDeleteProgram(shader.program);
		}
		shaders.clear();
	}
	
	public class UniformVar {
		
		private final String name;
		private final int location;
		
		private Object value;
		private boolean isDirty;
		private int lastTexUnit = -1;
		
		UniformVar(String name, int location) {
			this.name = name;
			this.location = location;
		}
		
		public Object get() {
			return this.value;
		}
		
		public void set(Object value) {
			this.value = value;
			this.isDirty = true;
			if(Shader.this.isActive()) {
				if(this.isTexture() && this.lastTexUnit < 0) {
					throw new IllegalArgumentException("You must bind a texture BEFORE binding the shader before you can hotswap them!");
				} else {
					this.upload(this.lastTexUnit);
				}
			}
		}
		
		public String getName() {
			return this.name;
		}
		
		public boolean isValid() {
			return this.location != -1;
		}
		
		public boolean isTexture() {
//			return this.value instanceof Texture;
			return false;
		}
		
		private void upload(int texUnit) {
			if(!this.isValid() || this.value == null || !this.isDirty) {
				return;
			}
			this.isDirty = false;
			
			Object value = get();
			if(value instanceof Integer) {
				glUniform1i(this.location, (int)value);
			} else if(value instanceof Float) {
				glUniform1f(this.location, (float)value);
			} else if(value instanceof Boolean) {
				glUniform1i(this.location, (boolean)value ? 1 : 0);
			} else if(value instanceof Vector2f) {
				Vector2f v = (Vector2f)value;
				glUniform2f(this.location, v.x, v.y);
			} else if(value instanceof Vector3f) {
				Vector3f v = (Vector3f)value;
				glUniform3f(this.location, v.x, v.y, v.z);
			} else if(value instanceof Vector4f) {
				Vector4f v = (Vector4f)value;
				glUniform4f(this.location, v.x, v.y, v.z, v.w);
			} else if(value instanceof Matrix4f) {
				((Matrix4f)value).get(TEMP_MAT4);
				glUniformMatrix4fv(this.location, false, TEMP_MAT4);
			} else if(value instanceof Color) {
				Color color = (Color)value;
				glUniform4f(this.location, color.r, color.g, color.b, color.a);
			} else {
				System.err.println("Uniform variable " + name + " has unrecognized type: " + value.getClass().getSimpleName());
			}
		}
		
	}
	
}