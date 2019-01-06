package com.countgandi.com.engine.Animation.renderer;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.UniformMat4Array;
import com.countgandi.com.engine.renderEngine.shaders.UniformMatrix;
import com.countgandi.com.engine.renderEngine.shaders.UniformSampler;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec3;

public class AnimatedModelShader extends ShaderProgram {

	private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
	private static final int DIFFUSE_TEX_UNIT = 0;

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/skybox/skyboxFragmentShader.txt";
	
	public UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	public UniformVec3 lightDirection = new UniformVec3("lightDirection");
	public UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", MAX_JOINTS);
	public UniformSampler diffuseMap = new UniformSampler("diffuseMap");

	/**
	 * Creates the shader program for the {@link AnimatedModelRenderer} by
	 * loading up the vertex and fragment shader code files. It also gets the
	 * location of all the specified uniform variables, and also indicates that
	 * the diffuse texture will be sampled from texture unit 0.
	 */
	public AnimatedModelShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		super.getAllUniformLocations(projectionViewMatrix, diffuseMap, lightDirection, jointTransforms);
		connectTextureUnits();
	}

	/**
	 * Indicates which texture unit the diffuse texture should be sampled from.
	 */
	private void connectTextureUnits() {
		super.start();
		diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
		super.stop();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
		super.bindAttribute(2, "in_normal");
		super.bindAttribute(3, "in_jointIndices");
		super.bindAttribute(4, "in_weights");
	}

}
