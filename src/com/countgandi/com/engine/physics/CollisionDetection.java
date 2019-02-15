package com.countgandi.com.engine.physics;

import org.lwjgl.util.vector.Vector3f;

public class CollisionDetection {

	private static final Vector3f emptyVector = new Vector3f(0, 0, 0);

	public static boolean intersectAxisAlignedBB(AxisAlignedBB a1, AxisAlignedBB a2) {
		if (a1.getPosition().x > a2.getPosition().x && a1.getPosition().x < a2.getPosition().x + a2.getSize().x || a2.getPosition().x > a1.getPosition().x && a2.getPosition().x < a1.getPosition().x + a1.getSize().x)
			if (a1.getPosition().y > a2.getPosition().y && a1.getPosition().y < a2.getPosition().y + a2.getSize().y || a2.getPosition().y > a1.getPosition().y && a2.getPosition().y < a1.getPosition().y + a1.getSize().y)
				if (a1.getPosition().z > a2.getPosition().z && a1.getPosition().z < a2.getPosition().z + a2.getSize().z || a2.getPosition().z > a1.getPosition().z && a2.getPosition().z < a1.getPosition().z + a1.getSize().z)
					return true;
		return false;
	}

	public static boolean intersectTriangleRay(Triangle triangle, Ray ray) {
		Vector3f point = new Vector3f();
		Vector3f u, v, n;

		// get triangle edge vectors and plane normal
		u = Vector3f.sub(triangle.getVertexs()[1], triangle.getVertexs()[0], null);
		v = Vector3f.sub(triangle.getVertexs()[2], triangle.getVertexs()[0], null);
		n = new Vector3f(u.x * v.x, u.y * v.y, u.z * u.z);

		if (n.equals(emptyVector))
			return false;
		Vector3f dir = Vector3f.sub(ray.getPoint1(), ray.getPoint(), null);
		Vector3f w0 = Vector3f.sub(ray.getPoint(), triangle.getVertexs()[0], null);
		float a = -Vector3f.dot(n, w0);
		float b = Vector3f.dot(n, dir);
		if (Math.abs(b) < 0.000001f) {
			return true;
		}

		float r = a / b;
		if (r < 0.0) {
			return false;
		}
		point = Vector3f.add(ray.getPoint(), new Vector3f(dir.x * r, dir.y * r, dir.z * r), null);
		float uu, uv, vv, wu, wv, D;
		uu = Vector3f.dot(u, u);
		uv = Vector3f.dot(u, v);
		vv = Vector3f.dot(v, v);
		Vector3f w = Vector3f.sub(point, triangle.getVertexs()[0], null);
		wu = Vector3f.dot(w, u);
		wv = Vector3f.dot(w, v);
		D = uv * uv - uu * vv;

		float s, t;
		s = (uv * wv - vv * wu) / D;
		if (s < 0.0 || s > 1.0) // I is outside T
			return false;
		t = (uv * wu - uu * wv) / D;
		if (t < 0.0 || (s + t) > 1.0) // I is outside T
			return false;

		return true;
	}

	public boolean intersectTriangle(Triangle triangle, Triangle triangle1) {
		boolean flag = false;

		return flag;
	}

}
