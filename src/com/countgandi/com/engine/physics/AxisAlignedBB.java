package com.countgandi.com.engine.physics;

import org.lwjgl.util.vector.Vector3f;

public class AxisAlignedBB {
	
	private Vector3f pos, size;
	
	public AxisAlignedBB (Vector3f pos, Vector3f size) {
		this.pos = pos;
		this.size = size;
	}
	
	public Vector3f getPosition() {
		return pos;
	}
	
	public Vector3f getSize() {
		return size;
	}

}
