package com.countgandi.com.engine.renderEngine.shaders;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.entities.Light;

public class StaticShader extends ShaderProgram {

	private static final int MaxLights = 4;

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/shaders/fragmentShader.txt";

	public UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	public UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	public UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	public UniformVec3 lightPosition[] = new UniformVec3[MaxLights];
	public UniformVec3 lightColor[] = new UniformVec3[MaxLights];
	public UniformVec3 attenuation[] = new UniformVec3[MaxLights];
	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	public UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	public UniformVec3 skyColor = new UniformVec3("skyColor");
	public UniformInt numberOfRows = new UniformInt("numberOfRows");
	public UniformVec2 offset = new UniformVec2("offset");
	public UniformVec4 plane = new UniformVec4("plane");

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		Uniform arrays[][] = new Uniform[3][MaxLights];
		for (int i = 0; i < MaxLights; i++) {
			lightPosition[i] = new UniformVec3("lightPosition[" + i + "]");
			lightColor[i] = new UniformVec3("lightColor[" + i + "]");
			attenuation[i] = new UniformVec3("attenuation[" + i + "]");
			arrays[0][i] = lightPosition[i];
			arrays[1][i] = lightColor[i];
			arrays[2][i] = attenuation[i];
		}
		super.getAllUniformLocations(arrays, transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity, useFakeLighting, skyColor, numberOfRows, offset, plane);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MaxLights; i++) {
			if (i < lights.size()) {
				lightPosition[i].loadVec3(lights.get(i).getPosition());
				lightColor[i].loadVec3(lights.get(i).getColor());
				attenuation[i].loadVec3(lights.get(i).getAttenuation());
			} else {
				lightPosition[i].loadVec3(new Vector3f(0, 0, 0));
				lightColor[i].loadVec3(new Vector3f(0, 0, 0));
				attenuation[i].loadVec3(new Vector3f(0, 0, 0));
			}
		}
	}

}
