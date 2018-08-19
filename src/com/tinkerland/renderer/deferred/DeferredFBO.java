package com.tinkerland.renderer.deferred;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.tinkerland.utils.Buffers;

public class DeferredFBO {

	private int width, height;
	
	private int fbo;
	private int gBuffer;
	private int nBuffer;
	private int sBuffer;
	private int dBuffer;

	public void bind(int w, int h) {
		this.resize(w, h);
		glClearColor(0f, 0f, 0f, 0f);
		glViewport(0, 0, w, h);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void delete() {
		if(fbo > 0) {
			glDeleteFramebuffers(fbo);
			glDeleteTextures(Buffers.fromArray(gBuffer, nBuffer, sBuffer, dBuffer));
		}
	}
	
	private void resize(int w, int h) {
		if(this.width != w || this.height != h) {
			this.delete();
		} else {
			return;
		}
		IntBuffer textures = BufferUtils.createIntBuffer(4);
		glGenTextures(textures);
		
		this.fbo = glGenFramebuffers();
		this.gBuffer = createTexture(textures.get(0), w, h, GL_RGBA, GL_RGBA);
		this.nBuffer = createTexture(textures.get(1), w, h, GL_RGBA, GL_RGBA);
		this.sBuffer = createTexture(textures.get(2), w, h, GL_RG, GL_RG);
		this.dBuffer = createTexture(textures.get(3), w, h, GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT);
		
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.gBuffer, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, this.nBuffer, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D, this.sBuffer, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,  GL_TEXTURE_2D, this.dBuffer, 0);
		glDrawBuffers(Buffers.fromArray(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2));
		
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		if(status != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Framebuffer is not complete! (status " + status + ")");
		}
		
		this.width  = w;
		this.height = h;
	}
	
	private int createTexture(int id, int w, int h, int internalFormat, int format) {
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		if(format == GL_DEPTH_COMPONENT) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);
		}
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, w, h, 0, format, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		return id;
	}
	
	public void bindGBuffer(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, this.gBuffer);
	}
	
	public void bindNormals(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, this.nBuffer);
	}
	
	public void bindDepth(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, this.dBuffer);
	}
	
	public void bindSpecular(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, this.sBuffer);
	}

}