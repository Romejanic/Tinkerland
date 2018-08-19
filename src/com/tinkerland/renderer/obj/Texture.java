package com.tinkerland.renderer.obj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import com.tinkerland.resources.Resources;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {

	private static final HashMap<String, Texture> textures = new HashMap<String, Texture>();

	private final String name;
	private int target;
	private int texture;
	
	private Texture(String name, int target, int texture) {
		this.name    = name;
		this.target  = target;
		this.texture = texture;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void bind() {
		glBindTexture(this.target, this.texture);
	}
	
	public void unbind() {
		glBindTexture(this.target, 0);
	}
	
	public static Texture get(String name) {
		if(textures.containsKey(name)) {
			return textures.get(name);
		}
		Texture t = new Texture(name, GL_TEXTURE_2D, glGenTextures());
		try {
			PNGDecoder png = new PNGDecoder(Resources.getResource("textures/" + name + ".png"));
			ByteBuffer buf = BufferUtils.createByteBuffer(png.getWidth() * png.getHeight() * 4);
			png.decode(buf, png.getWidth() * 4, Format.RGBA);
			buf.flip();
			
			t.bind();
			glTexParameteri(t.target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(t.target, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(t.target, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(t.target, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameterf(t.target, GL_TEXTURE_LOD_BIAS, -1f);
			glTexImage2D(t.target, 0, GL_RGBA8, png.getWidth(), png.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
			glGenerateMipmap(t.target);
			t.unbind();
		} catch(Exception e) {
			System.err.println("Failed to load texture: " + name + "!");
			e.printStackTrace(System.err);
			if(t.texture > 0) {
				t.unbind();
				glDeleteTextures(t.texture);
				t.texture = 0;
			}
		}
		textures.put(name, t);
		return t;
	}
	
	public static void deleteAll() {
		for(Texture t : textures.values()) {
			if(t.texture > 0) {
				glDeleteTextures(t.texture);
			}
		}
		textures.clear();
	}
	
}