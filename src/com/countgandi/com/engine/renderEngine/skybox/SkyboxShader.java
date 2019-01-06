package com.countgandi.com.engine.renderEngine.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.UniformFloat;
import com.countgandi.com.engine.renderEngine.shaders.UniformInt;
import com.countgandi.com.engine.renderEngine.shaders.UniformMatrix;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec3;
import com.countgandi.com.game.entities.Camera;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxFragmentShader.txt";
	
	private static final float RotateSpeed = 1f;

	public UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	public UniformVec3 fogColor = new UniformVec3("fogColor");
	private UniformInt cubeMap = new UniformInt("cubeMap");
	private UniformInt cubeMap2 = new UniformInt("cubeMap2");
	public UniformFloat blendFactor = new UniformFloat("blendFactor");
	
	private float rotation = 0;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		super.getAllUniformLocations(projectionMatrix, viewMatrix, fogColor, cubeMap, cubeMap2, blendFactor);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f(camera.getViewMatrix());
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += RotateSpeed * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		viewMatrix.loadMatrix(matrix);
	}
	
	public void connectTextureUnits() {
		cubeMap.loadInt(0);
		cubeMap2.loadInt(1);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}