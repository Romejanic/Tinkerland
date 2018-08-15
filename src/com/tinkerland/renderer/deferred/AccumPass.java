package com.tinkerland.renderer.deferred;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.tinkerland.camera.IView;
import com.tinkerland.lighting.Light;
import com.tinkerland.lighting.Light.LightType;
import com.tinkerland.renderer.obj.Mesh;
import com.tinkerland.renderer.obj.Mesher;
import com.tinkerland.renderer.obj.Shader;
import com.tinkerland.scene.Scene;
import com.tinkerland.utils.Buffers;
import com.tinkerland.utils.Mathf;
import com.tinkerland.utils.Temp;

public class AccumPass {

	private final Matrix4f tempProjMat = new Matrix4f();
	private final Matrix4f tempViewMat = new Matrix4f();
	
	private int width, height;

	private int fbo;
	private int diffuse;
	private int specular;

	private Mesh quad;
	private Shader[] shaders;

	public AccumPass() {
		this.quad = Mesher.getFullscreenQuad();
		this.shaders = new Shader[LightType.values().length];

		for(LightType type : LightType.values()) {
			if(type == LightType.SPOT) {
				continue;
			}
			this.shaders[type.ordinal()] = Shader.get("deferred/accum_" + type.name().toLowerCase());
		}
	}
	
	public void bindDiffuse(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, this.diffuse);
	}
	
	public void bindSpecular(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, this.specular);
	}

	@SuppressWarnings("unchecked")
	public void drawPass(int w, int h, IView camera, Scene scene, DeferredFBO fbo) {
		this.resize(w, h);
		glViewport(0, 0, w, h);
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glClear(GL_COLOR_BUFFER_BIT);
		
		Light[] lights = scene.getLights();
		if(lights.length > 0) {
			fbo.bindNormals(0);
			fbo.bindDepth(1);
			Matrix4f invProjMat = camera.getProjectionMatrix().invert(tempProjMat);
			Matrix4f invViewMat = camera.getViewMatrix().invert(tempViewMat);
			
			ArrayList<Light>[] sortedLights = new ArrayList[LightType.values().length];
			for(Light light : lights) {
				if(sortedLights[light.type.ordinal()] == null) {
					sortedLights[light.type.ordinal()] = new ArrayList<Light>();
				}
				sortedLights[light.type.ordinal()].add(light);
			}
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			this.quad.bind();
			for(LightType type : LightType.values()) {
				if(sortedLights[type.ordinal()] == null || sortedLights[type.ordinal()].isEmpty()) {
					continue;
				}
				Shader shader = this.shaders[type.ordinal()];
				shader.bind();
				shader.uniform("normals").set(0);
				shader.uniform("depth").set(1);
				shader.uniform("cameraPos").set(camera.getPosition());
				shader.uniform("invProjMat").set(invProjMat);
				shader.uniform("invViewMat").set(invViewMat);
				for(Light light : sortedLights[type.ordinal()]) {
					shader.uniform("lightSource.color").set(light.color);
					shader.uniform("lightSource.intensity").set(light.intensity);
					switch(type) {
					case DIRECTIONAL:
						Vector3f lightDir = Mathf.rotationToVector(light.rotation, Temp.VEC3).negate();
						shader.uniform("lightSource.direction").set(lightDir);
						break;
					case OMNI:
						shader.uniform("lightSource.range").set(light.range);
						shader.uniform("lightSource.position").set(light.position);
						break;
					case SPOT:
						lightDir = Mathf.rotationToVector(light.rotation, Temp.VEC3).negate();
						shader.uniform("lightSource.direction").set(lightDir);
						shader.uniform("lightSource.range").set(light.range);
						shader.uniform("lightSource.position").set(light.position);
						shader.uniform("lightSource.spotAngle").set(light.spotAngle);
						break;
					default:
						continue;
					}
					this.quad.draw();
				}
				shader.unbind();
			}
			this.quad.unbind();
			glDisable(GL_BLEND);
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	private void resize(int w, int h) {
		if(this.width != w || this.height != h) {
			this.delete();
		} else {
			return;
		}
		IntBuffer textures = BufferUtils.createIntBuffer(2);
		glGenTextures(textures);

		this.fbo = glGenFramebuffers();
		this.diffuse  = createTexture(textures.get(0), w, h, GL_RGB32F, GL_RGB);
		this.specular = createTexture(textures.get(1), w, h, GL_RGB32F, GL_RGB);

		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.diffuse, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, this.specular, 0);
		glDrawBuffers(Buffers.fromArray(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1));

		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		if(status != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Failed to create accumulation framebuffer! (status " + status + ")");
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
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, w, h, 0, format, GL_FLOAT, (ByteBuffer)null);
		return id;
	}

	public void delete() {
		glDeleteFramebuffers(this.fbo);
		glDeleteTextures(Buffers.fromArray(this.diffuse, this.specular));
	}

}