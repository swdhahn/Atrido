package com.countgandi.com.engine.renderEngine.Animation.renderer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;

public class AnimatedModelShader extends ShaderProgram {

	private static final int MAX_JOINTS = 50;// max number of joints in a skeleton

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxFragmentShader.txt";
	
	private int location_projectionViewMatrix;
	private int location_lightDirection;
	private int location_jointTransforms[];
	private int location_diffuseMap;

	public AnimatedModelShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		connectTextureUnits();
	}

	private void connectTextureUnits() {
		start();
		super.loadInt(location_diffuseMap, 0);
		stop();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
		super.bindAttribute(2, "in_normal");
		super.bindAttribute(3, "in_jointIndices");
		super.bindAttribute(4, "in_weights");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionViewMatrix = super.getUniformLocation("projectionViewMatrix");
		location_lightDirection = super.getUniformLocation("lightDirection");
		location_diffuseMap = super.getUniformLocation("diffuseMap");
		
		location_jointTransforms = new int[MAX_JOINTS];
		for(int i = 0; i < MAX_JOINTS; i++) {
			location_jointTransforms[i] = super.getUniformLocation("jointTransforms[" + i + "]");
		}
	}
	
	public void loadProjectionViewMatrix(Matrix4f projectionViewMatrix) {
		super.loadMatrix(location_projectionViewMatrix, projectionViewMatrix);
	}
	
	public void loadLightDirection(Vector3f lightDirection) {
		super.loadVector(location_lightDirection, lightDirection);
	}
	
	public void loadJointTransformations(Matrix4f[] jointTransformations) {
		for(int i = 0; i < jointTransformations.length; i++) {
			super.loadMatrix(location_jointTransforms[i], jointTransformations[i]);
		}
	}

}
