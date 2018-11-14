package com.countgandi.com.guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private Vector2f rotation;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		rotation = new Vector2f(0, 0);
	}
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale, Vector2f rot) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rot;
	}
	
	public int getTexture() {
		return texture;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
	public Vector2f getRot() {
		return rotation;
	}
}
