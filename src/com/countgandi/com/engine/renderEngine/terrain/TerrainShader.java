package com.countgandi.com.engine.renderEngine.terrain;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.renderEngine.shaders.ShaderProgram;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.game.worldGen.World;

public class TerrainShader extends ShaderProgram {

	private static final int MaxLights = 4;

	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/terrain/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/terrain/terrainFragmentShader.txt";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_Texture0;
	private int location_Texture1;
	private int location_Texture2;
	private int location_Texture3;
	private int location_Texture4;
	private int location_plane;
	private int location_tiling;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		start();
		super.loadFloat(location_tiling, World.terrainSideAmount / 10.0f);
		stop();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColor");
		location_Texture0 = super.getUniformLocation("texture0");
		location_Texture1 = super.getUniformLocation("texture1");
		location_Texture2 = super.getUniformLocation("texture2");
		location_Texture3 = super.getUniformLocation("texture3");
		location_Texture4 = super.getUniformLocation("texture4");
		location_plane = super.getUniformLocation("plane");
		location_tiling = super.getUniformLocation("tileDivision");

		location_lightPosition = new int[MaxLights];
		location_lightColor = new int[MaxLights];
		location_attenuation = new int[MaxLights];
		for (int i = 0; i < MaxLights; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}

	}

	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}

	public void loadShineVariable(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void connectTextureUnits() {
		super.loadInt(location_Texture0, 0);
		super.loadInt(location_Texture1, 1);
		super.loadInt(location_Texture2, 2);
		super.loadInt(location_Texture3, 3);
		super.loadInt(location_Texture4, 4);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MaxLights; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadSkyColor(Vector3f skycolor) {
		super.loadVector(location_skyColor, skycolor);
	}
	
	public void loadTiling(float tiling) {
		super.loadFloat(location_tiling, tiling);
	}

}
