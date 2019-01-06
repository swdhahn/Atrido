package com.countgandi.com.engine.renderEngine.font.fontRendering;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.UniformFloat;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec2;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec3;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/font/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/font/fontRendering/fontFragment.txt";
	
	public UniformVec3 color = new UniformVec3("color");
	public UniformVec3 outlineColor = new UniformVec3("outlineColor");
	public UniformVec2 translation = new UniformVec2("translation");
	public UniformVec2 offset = new UniformVec2("offset");
	public UniformFloat width = new UniformFloat("width");
	public UniformFloat edge = new UniformFloat("edge");
	public UniformFloat borderWidth = new UniformFloat("borderWidth");
	public UniformFloat borderEdge = new UniformFloat("borderEdge");
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		super.getAllUniformLocations(color, outlineColor, translation, offset, width, edge, borderWidth, borderEdge);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

}
