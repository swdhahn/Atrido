package com.countgandi.com.engine.renderEngine.water;

public class WaterTile {
	
	public static final int SIZE = 1024, VERTEX_COUNT = 2;
	
	private float height;
	private float x,z;
	
	public WaterTile(float gridX, float gridY, float height){
		this.x = SIZE * gridX;
		this.z = SIZE * gridY;
		this.height = height;
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
