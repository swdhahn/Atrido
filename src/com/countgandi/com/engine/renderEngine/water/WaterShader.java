package com.countgandi.com.engine.renderEngine.water;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.UniformFloat;
import com.countgandi.com.engine.renderEngine.shaders.UniformInt;
import com.countgandi.com.engine.renderEngine.shaders.UniformMatrix;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec3;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/water/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/water/waterFragment.glsl";

	public UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	public UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	public UniformInt reflectionTexture = new UniformInt("reflectionTexture");
	public UniformInt refractionTexture = new UniformInt("refractionTexture");
	public UniformInt dudvMap = new UniformInt("dudvMap");
	public UniformFloat moveFactor = new UniformFloat("moveFactor");
	public UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	public UniformInt normalMap = new UniformInt("normalMap");
	public UniformVec3 lightColor = new UniformVec3("lightColor");
	public UniformVec3 lightPosition = new UniformVec3("lightPosition");
	public UniformInt depthMap = new UniformInt("depthMap");
	public UniformFloat tiling = new UniformFloat("tiling");

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		super.getAllUniformLocations(modelMatrix, projectionViewMatrix, reflectionTexture, refractionTexture, dudvMap, moveFactor, cameraPosition, normalMap, lightColor, lightPosition, depthMap, tiling);
		start();
		tiling.loadFloat(WaterTile.SIZE / 10);
		stop();
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
	}

	public void connectTextureUnits() {
		reflectionTexture.loadInt(0);
		refractionTexture.loadInt(1);
		dudvMap.loadInt(2);
		normalMap.loadInt(3);
		depthMap.loadInt(4);
	}

}
