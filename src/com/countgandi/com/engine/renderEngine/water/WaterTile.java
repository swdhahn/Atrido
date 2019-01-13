package com.countgandi.com.engine.renderEngine.water;

public class WaterTile {
	
	public static final int VERTEX_COUNT = 100;

	public static int SIZE = 1024;
	
	private float height;
	private float x, z;
	public float scale;
	
	public WaterTile(float x, float y, float height, float scale){
		this.x = x * SIZE;
		this.z = y * SIZE;
		this.height = height;
		this.scale = scale;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
