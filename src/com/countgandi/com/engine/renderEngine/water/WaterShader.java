package com.countgandi.com.engine.renderEngine.water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/water/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/water/waterFragment.glsl";

	private int location_modelMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_dudvMap;
	private int location_normalMap;
	private int location_depthMap;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_waterColor;

	private int location_moveFactor;
	private int location_cameraPosition;
	private int location_lightColor;
	private int location_lightPosition;

	public int location_tiling;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		start();
		super.loadFloat(location_tiling, WaterTile.SIZE / 100 * 4);
		stop();
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_dudvMap = super.getUniformLocation("dudvMap");
		location_depthMap = super.getUniformLocation("depthMap");
		location_normalMap = super.getUniformLocation("normalMap");
		location_reflectionTexture = super.getUniformLocation("reflectionTexture");
		location_refractionTexture = super.getUniformLocation("refractionTexture");
		location_moveFactor = super.getUniformLocation("moveFactor");
		location_cameraPosition = super.getUniformLocation("cameraPosition");
		location_waterColor = super.getUniformLocation("waterColor");
		location_lightColor = super.getUniformLocation("lightColor");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_tiling = super.getUniformLocation("tiling");
	}

	public void connectTextureUnits() {
		super.loadInt(location_dudvMap, 0);
		super.loadInt(location_normalMap, 1);
		super.loadInt(location_depthMap, 2);
		super.loadInt(location_reflectionTexture, 3);
		super.loadInt(location_refractionTexture, 4);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_modelMatrix, transformationMatrix);
	}

	public void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadCameraPos(Vector3f pos) {
		super.loadVector(location_cameraPosition, pos);
	}

	public void loadTiling(float tiling) {
		super.loadFloat(location_tiling, tiling);
	}

	public void loadMoveFactor(float moveFactor) {
		super.loadFloat(location_moveFactor, moveFactor);
	}

	public void loadLight(Vector3f lightColor, Vector3f lightPosition) {
		super.loadVector(location_lightColor, lightColor);
		super.loadVector(location_lightPosition, lightPosition);
	}
	
	public void loadWaterColor(Vector3f color) {
		super.loadVector(location_waterColor, color);
	}

}
