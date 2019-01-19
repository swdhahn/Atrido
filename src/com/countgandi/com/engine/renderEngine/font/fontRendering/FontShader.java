package com.countgandi.com.engine.renderEngine.font.fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/font/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/font/fontRendering/fontFragment.txt";
	
	private int location_color;
	private int location_outlineColor;
	private int location_translation;
	private int location_offset;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_outlineColor = super.getUniformLocation("outlineColor");
		location_translation = super.getUniformLocation("translation");
		location_offset = super.getUniformLocation("offset");
		location_edge = super.getUniformLocation("edge");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_width = super.getUniformLocation("width");
		location_borderWidth = super.getUniformLocation("borderWidth");
	}
	
	public void loadFontLol(Vector2f translation, Vector2f offset, Vector3f color, Vector3f outlineColor, float width, float borderWidth, float edge, float borderEdge) {
		super.load2DVector(location_translation, translation);
		super.load2DVector(location_offset, offset);
		super.loadVector(location_color, color);
		super.loadVector(location_outlineColor, outlineColor);
		super.loadFloat(location_width, width);
		super.loadFloat(location_borderWidth, borderWidth);
		super.loadFloat(location_edge, edge);
		super.loadFloat(location_borderEdge, borderEdge);
	}

}
