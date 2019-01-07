package com.countgandi.com.engine.renderEngine.guis;

import org.lwjgl.util.vector.Vector2f;

import com.countgandi.com.engine.renderEngine.textures.Texture;

public class GuiTexture {
	
	private Texture texture;
	private Vector2f position;
	private Vector2f scale;
	private Vector2f rotation;
	
	public GuiTexture(Texture texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		rotation = new Vector2f(0, 0);
	}
	
	public GuiTexture(Texture texture, Vector2f position, Vector2f scale, Vector2f rot) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rot;
	}
	
	public Texture getTexture() {
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
