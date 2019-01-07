package com.countgandi.com.engine.renderEngine.terrain;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.engine.renderEngine.shaders.Uniform;
import com.countgandi.com.engine.renderEngine.shaders.UniformFloat;
import com.countgandi.com.engine.renderEngine.shaders.UniformInt;
import com.countgandi.com.engine.renderEngine.shaders.UniformMatrix;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec3;
import com.countgandi.com.engine.renderEngine.shaders.UniformVec4;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.game.worldGen.World;

public class TerrainShader extends ShaderProgram {

	private static final int MaxLights = 4;

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/terrain/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/terrain/terrainFragmentShader.txt";

	public UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	public UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	public UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	public UniformVec3 lightPosition[] = new UniformVec3[MaxLights];
	public UniformVec3 lightColor[] = new UniformVec3[MaxLights];
	public UniformVec3 attenuation[] = new UniformVec3[MaxLights];
	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	public UniformVec3 skyColor = new UniformVec3("skyColor");
	private UniformInt texture0 = new UniformInt("texture0");
	private UniformInt texture1 = new UniformInt("texture1");
	private UniformInt texture2 = new UniformInt("texture2");
	private UniformInt texture3 = new UniformInt("texture3");
	private UniformInt texture4 = new UniformInt("texture4");
	public UniformVec4 plane = new UniformVec4("plane");
	private UniformFloat tiling = new UniformFloat("tileDivision");

	public TerrainShader() {
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
		super.getAllUniformLocations(arrays, transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity, skyColor, texture0, texture1, texture2, texture3, texture4, plane, tiling);
		start();
		tiling.loadFloat(World.terrainSideAmount / 100.0f);
		stop();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	public void connectTextureUnits() {
		texture0.loadInt(0);
		texture1.loadInt(1);
		texture2.loadInt(2);
		texture3.loadInt(3);
		texture4.loadInt(4);
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
