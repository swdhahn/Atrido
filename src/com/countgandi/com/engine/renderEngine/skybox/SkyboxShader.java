package com.countgandi.com.engine.renderEngine.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.game.entities.Camera;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxFragmentShader.glsl";
	
	private static final float RotateSpeed = 1f;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;
	
	private float rotation = 0;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f(camera.getViewMatrix());
		//matrix.m30 = 0;
		//matrix.m31 = 0;
		//matrix.m32 = 0;
		//rotation += RotateSpeed * DisplayManager.getFrameTimeSeconds();
		//Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadFogColor(Vector3f color) {
		super.loadVector(location_fogColor, color);
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}