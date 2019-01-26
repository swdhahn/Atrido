package com.countgandi.com.engine.renderEngine.models;

import com.countgandi.com.engine.renderEngine.textures.ModelTexture;

public class TexturedModel {

	public boolean isInstanced = false;
	public int vbo = -1;
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
	
}
