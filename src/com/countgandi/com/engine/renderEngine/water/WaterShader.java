package com.countgandi.com.engine.renderEngine.water;

import org.lwjgl.util.vector.Matrix4f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Light;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/water/waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_dudvMap;
	private int location_moveFactor;
	private int location_cameraPosition;
	private int location_normalMap;
	private int location_lightColor;
	private int location_lightPosition;
	private int location_depthMap;
	private int location_tiling;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		start();
		super.loadFloat(location_tiling, WaterTile.TILE_SIZE / 10);
		stop();
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflectionTexture = super.getUniformLocation("reflectionTexture");
		location_refractionTexture = super.getUniformLocation("refractionTexture");
		location_dudvMap = super.getUniformLocation("dudvMap");
		location_normalMap = super.getUniformLocation("normalMap");
		location_moveFactor = super.getUniformLocation("moveFactor");
		location_cameraPosition = super.getUniformLocation("cameraPosition");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_depthMap = super.getUniformLocation("depthMap");
		location_tiling = super.getUniformLocation("tiling");
	}
	
	public void loadMoveFactor(float factor) {
		super.loadFloat(location_moveFactor, factor);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
		super.loadInt(location_dudvMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_depthMap, 4);
	}
	
	public void loadLight(Light sun) {
		super.loadVector(location_lightColor, sun.getColor());
		super.loadVector(location_lightPosition, sun.getPosition());
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
