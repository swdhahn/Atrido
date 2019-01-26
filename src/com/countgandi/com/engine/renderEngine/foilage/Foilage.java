package com.countgandi.com.engine.renderEngine.foilage;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;

public class Foilage {

	public float scale;
	public Vector3f rot, pos;
	public TexturedModel model;
	public Matrix4f transformation;

	public Foilage(TexturedModel model, Vector3f pos, Vector3f rot, float scale) {
		this.model = model;
		this.pos = pos;
		this.scale = scale;
		this.rot = rot;
		this.transformation = Maths.createTransformationMatrix(pos, rot.x, rot.y, rot.z, scale);
	}

}
