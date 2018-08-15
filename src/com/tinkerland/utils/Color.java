package com.tinkerland.utils;

public class Color {

	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED   = new Color(255, 0, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color BLUE  = new Color(0, 0, 255);
	
	public float r, g, b, a;
	
	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1f);
	}
	
	public Color(int r, int g, int b, int a) {
		this.set(r, g, b, a);
	}
	
	public Color(float r, float g, float b, float a) {
		this.set(r, g, b, a);
	}
	
	public Color set(int r, int g, int b) {
		return set(r, g, b, 255);
	}
	
	public Color set(int r, int g, int b, int a) {
		return set((float)r / 255f, (float)g / 255f, (float)b / 255f, (float)a / 255f);
	}
	
	public Color set(float r, float g, float b) {
		return set(r, g, b, 1f);
	}
	
	public Color set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[r=" + r + ",g=" + g + ",b=" + b + ",a=" + a + "]";
	}
	
	public static Color random() {
		return new Color(Mathf.random(), Mathf.random(), Mathf.random());
	}
	
}