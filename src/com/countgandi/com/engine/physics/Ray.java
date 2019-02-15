package com.countgandi.com.engine.physics;

import org.lwjgl.util.vector.Vector3f;

public class Ray {
	
	private Vector3f point, point1;
	
	public Ray(Vector3f point, Vector3f point1) {
		this.point1 = point1;
		this.point = point;
	}
	
	public Vector3f getPoint1() {
		return point1;
	}
	
	public void setPoint1(Vector3f point1) {
		this.point1 = point1;
	}
	
	public Vector3f getPoint() {
		return point;
	}
	
	public void setPoint(Vector3f point) {
		this.point = point;
	}

}
