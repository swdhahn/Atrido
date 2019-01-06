package com.countgandi.com.engine.renderEngine.particles;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.UniformMatrix;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/particles/particleFShader.txt";

	public UniformMatrix modelViewMatrix = new UniformMatrix("modelViewMatrix");
	public UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		super.getAllUniformLocations(modelViewMatrix, projectionMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
