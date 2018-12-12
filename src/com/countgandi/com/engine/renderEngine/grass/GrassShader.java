package com.countgandi.com.engine.renderEngine.grass;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;

public class GrassShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER = "src/com/countgandi/com/engine/renderEngine/grass/vertex.glsl", FRAGMENT_SHADER = "src/com/countgandi/com/engine/renderEngine/grass/fragment.glsl";

	public GrassShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	@Override
	protected void getAllUniformLocations() {
		
	}

}
