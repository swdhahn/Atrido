package com.countgandi.com.engine.physics;

import org.lwjgl.util.vector.Vector3f;

public class Triangle {

	private Vector3f[] vertexs = new Vector3f[3];

	public Triangle() {
		vertexs[0] = new Vector3f(0, 0, 0);
		vertexs[1] = new Vector3f(0, 0, 0);
		vertexs[2] = new Vector3f(0, 0, 0);
	}

	public Triangle(Vector3f vertex0, Vector3f vertex1, Vector3f vertex2) {
		vertexs[0] = vertex0;
		vertexs[1] = vertex1;
		vertexs[2] = vertex2;
	}

	public Triangle(Vector3f[] vertexs) {
		this.vertexs = vertexs;
	}

	public Vector3f[] getVertexs() {
		return vertexs;
	}

	public void setVertexs(Vector3f[] vertexs) {
		this.vertexs = vertexs;
	}

}
