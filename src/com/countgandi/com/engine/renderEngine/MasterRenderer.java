package com.countgandi.com.engine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.countgandi.com.engine.renderEngine.grass.GrassRenderer;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.shaders.StaticShader;
import com.countgandi.com.engine.renderEngine.skybox.SkyboxRenderer;
import com.countgandi.com.engine.renderEngine.terrain.Terrain;
import com.countgandi.com.engine.renderEngine.terrain.TerrainRenderer;
import com.countgandi.com.engine.renderEngine.terrain.TerrainShader;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.game.structures.Structure;

public class MasterRenderer {

	private static final Vector3f skyColor = new Vector3f(0.5444F, 0.62F, 0.69F);// (0.5F, 0.5F, 0.5F);

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.2f;
	private static final float FAR_PLANE = 1000;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();

	private EntityRenderer renderer;
	private StructureRenderer structureRenderer;
	private TerrainRenderer terrainRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	public Map<TexturedModel, List<Entity>> grass = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Structure>> structures = new HashMap<TexturedModel, List<Structure>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private SkyboxRenderer skyboxRenderer;
	private GrassRenderer grassRenderer;
	private Loader loader;

	public MasterRenderer(Loader loader) {
		this.loader = loader;
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		structureRenderer = new StructureRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		grassRenderer = new GrassRenderer(projectionMatrix);
	}

	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();

		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(skyColor);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(skyColor);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		structureRenderer.render(structures);
		shader.stop();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(skyColor);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		grassRenderer.shader.start();
		grassRenderer.shader.loadClipPlane(clipPlane);
		grassRenderer.shader.loadSkyColor(skyColor);
		grassRenderer.shader.loadLights(lights);
		grassRenderer.shader.loadViewMatrix(camera);
		grassRenderer.render(grass);
		grassRenderer.shader.stop();

		skyboxRenderer.render(camera, skyColor);

		terrains.clear();
		entities.clear();
		structures.clear();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processStructure(Structure structure) {
		TexturedModel structureModel = structure.getModel();
		List<Structure> batch = structures.get(structureModel);
		if (batch != null) {
			batch.add(structure);
		} else {
			List<Structure> newBatch = new ArrayList<Structure>();
			newBatch.add(structure);
			structures.put(structureModel, newBatch);
		}
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processGrass(Vector3f pos, Vector3f rot, Handler handler) {
		Entity entity = new Entity(Assets.TexturedModels.grass, pos, rot, 2f, handler) {};
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = grass.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			grass.put(entityModel, newBatch);
		}
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		grassRenderer.shader.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public Loader getLoader() {
		return loader;
	}

}
