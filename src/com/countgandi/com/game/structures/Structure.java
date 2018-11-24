package com.countgandi.com.game.structures;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.game.Handler;

public abstract class Structure {

	protected TexturedModel model;
	protected Vector3f position, rotation;
	protected float scale;
	protected Handler handler;

	public Structure(TexturedModel model, Vector3f position, Vector3f rotation, float scale, Handler handler) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.handler = handler;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void tick() {

	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Used if you need an extra OpenGL code for rendering an entity
	 */
	public void render() {

	}

	/**
	 * Used if you need an extra OpenGL code for ending the rendering of an entity
	 */
	public void endRender() {

	}

}
