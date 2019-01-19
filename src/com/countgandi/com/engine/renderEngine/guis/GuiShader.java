package com.countgandi.com.engine.renderEngine.guis;

import org.lwjgl.util.vector.Matrix4f;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/guis/guiFragmentShader.txt";
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

}