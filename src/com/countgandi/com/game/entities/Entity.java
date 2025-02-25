package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.audio.Source;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.shaders.StaticShader;
import com.countgandi.com.game.Handler;

public abstract class Entity {

	private TexturedModel model;
	protected Vector3f position;
	protected float rotX, rotY, rotZ;
	protected float scale = 1;
	protected Handler handler;
	protected Source source;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, Vector3f rot, Handler handler) {
		this.model = model;
		this.position = position;
		this.rotX = rot.x;
		this.rotY = rot.y;
		this.rotZ = rot.z;
		this.handler = handler;
		source = new Source();
	}
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rot, float scale, Handler handler) {
		this(model, position, rot, handler);
		this.scale = scale;
	}

	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return (float) column / (float) model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureIndex % model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void tick() {
		source.setPosition(position.x, position.y, position.z);
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
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
	
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Source getSource() {
		return source;
	}

	/**
	 * Used if you need an extra OpenGL code for rendering an entity
	 */
	public void render(StaticShader shader) {

	}

	/**
	 * Used if you need an extra OpenGL code for ending the rendering of an entity
	 */
	public void endRender(StaticShader shader) {

	}

}
