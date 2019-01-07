package com.countgandi.com.engine.renderEngine.guis;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.UniformMatrix;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/guis/guiFragmentShader.txt";

	public UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		super.getAllUniformLocations(transformationMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}