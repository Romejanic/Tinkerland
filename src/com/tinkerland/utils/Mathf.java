package com.tinkerland.utils;

import java.util.Random;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Mathf {

	public static final Vector3fc UP = new Vector3f(0f, 1f, 0f).toImmutable();
	
	public static final float PI = (float)Math.PI;
	public static final float E  = (float)Math.E;
	
	private static final float DEG2RAD  = PI / 180f;
	private static final float RAD2DEG  = 180f / PI;
	public static final float PI_OVER_2 = PI / 2f;
	public static final float PI_OVER_4 = PI / 4f;
	public static final float TWO_PI    = PI * 2f;
	
	private static final float[] SIN_TABLE = new float[360 * 5];
	private static final Random RANDOM     = new Random();
	
	public static float rad(float deg) {
		return DEG2RAD * deg;
	}
	
	public static float deg(float rad) {
		return RAD2DEG * rad;
	}
	
	public static Vector3f rad(Vector3f deg) {
		return Temp.VEC3.set(rad(deg.x), rad(deg.y), rad(deg.z));
	}
	
	public static Vector3f deg(Vector3f rad) {
		return Temp.VEC3.set(deg(rad.x), deg(rad.y), deg(rad.z));
	}
	
	public static float sin(float x) {
		return SIN_TABLE[Math.floorMod((int)((x/TWO_PI)*SIN_TABLE.length), SIN_TABLE.length)];
	}
	
	public static float cos(float x) {
		return sin(x + PI_OVER_2);
	}
	
	public static float clamp(float a, float b, float x) {
		return x < a ? a : x > b ? b : x;
	}
	
	public static Vector3f rotationToVector(Vector3f angles) {
		return rotationToVector(angles, null);
	}
	
	public static Vector3f rotationToVector(Vector3f angles, Vector3f dest) {
		float sinX = sin(angles.x);
		float cosX = cos(angles.x);
		float sinY = sin(angles.y);
		float cosY = cos(angles.y);

		if(dest == null) {
			dest = new Vector3f();
		}
		return dest.set(sinY * cosX, -sinX, -cosY * cosX).normalize();
	}
	
	public static float random() {
		return RANDOM.nextFloat();
	}
	
	public static float random(float min, float max) {
		return min + (max - min) * random();
	}
	
	static {
		for(int i = 0; i < SIN_TABLE.length; i++) {
			SIN_TABLE[i] = (float)Math.sin(((double)i/(double)SIN_TABLE.length) * 2d * Math.PI);
		}
	}
	
}