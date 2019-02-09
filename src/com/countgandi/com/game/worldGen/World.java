package com.countgandi.com.game.worldGen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.creationEngine.Main;
import com.countgandi.com.creationEngine.menus.SessionObject;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.terrain.PerlinNoise;
import com.countgandi.com.engine.renderEngine.terrain.Terrain;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Game;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Light;

public class World {

	public static int SEED = 0;
	public static Random random = new Random();
	public static final int terrainSideAmount = 2 * Terrain.SIZE;
	private Thread thread;

	public ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	private ArrayList<TTerrain> tempTerrains = new ArrayList<TTerrain>();

	private Handler handler;

	public World(Handler handler) {
		this.handler = handler;
	}
	
	private boolean proceduralTerrainGenerating = false;

	public void generateWorld() {
		SEED = new Random().nextInt(1000000000);
		random.setSeed(SEED);
		Game.HEADER2.setText("Seed: " + SEED);
		try {
			handler.waters.add(new WaterTile(0, 0, 0));
			// terrains.add(new Terrain(0, 0, Assets.TERRAIN, Assets.loader));
		} catch (Exception e) {
			e.printStackTrace();
		}
		proceduralTerrainGenerating = true;
		proceduralTerrainGen();
	}
	
	public void stopTerrain() {
		proceduralTerrainGenerating = false;
	}

	private void proceduralTerrainGen() {
		thread = new Thread() {
			@Override
			public void run() {
				int terrainId = 0;
				while (proceduralTerrainGenerating) {
					for(int x = -1; x < 2; x++) {
						for(int z = -1; z < 2; z++) {
							Vector3f pos = new Vector3f(handler.getCamera().getPosition().x + x * Terrain.SIZE, 0, handler.getCamera().getPosition().z + z * Terrain.SIZE);
							if (getTerrainStandingOn(pos) == null && terrainNotAdded(pos)) {
								TTerrain t = new TTerrain((int) (pos.x / Terrain.SIZE), (int) (pos.z / Terrain.SIZE), terrainId);
								tempTerrains.add(t);
							}
						}
					}
				}
			}
		};
		thread.start();
	}
	private boolean terrainNotAdded(Vector3f pos) {
		for(int i = 0; i < terrains.size(); i++) {
			if(terrains.get(i).getX()  / Terrain.SIZE == (int)(pos.x / Terrain.SIZE) && terrains.get(i).getZ() / Terrain.SIZE == (int) (pos.z / Terrain.SIZE)) {
				return false;
			}
		}
		return true;
	}

	public void updateTerrain() {
		for (Iterator<TTerrain> iterator = tempTerrains.iterator(); iterator.hasNext();) {
			TTerrain t = iterator.next();
			System.out.println("x: " + t.pos.x + "   z: " + t.pos.z);
			terrains.add(t.createTerrain(handler, Assets.loader));
			iterator.remove();
		}
	}

	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
		if (handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(terrain));
		}
	}

	public void addWater(WaterTile waterTile) {
		handler.waters.add(waterTile);
		if (handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(waterTile));
		}
	}

	public void renderWorld(MasterRenderer renderer) {
		for (Terrain t : terrains) {
			renderer.processTerrain(t);
		}
	}

	public float getHeight(Vector3f position) {
		if (getTerrainStandingOn(position) == null) {
			return 0;
		}
		try {
			return getTerrainStandingOn(position).getHeightOfTerrain(position.x, position.z);
		} catch (NullPointerException e) {
			return 0;
		}
	}

	public Terrain getTerrainStandingOn(Vector3f position) {
		for (int i = 0; i < terrains.size(); i++) {
			Terrain t = terrains.get(i);
			if(t == null || position == null)
				terrains.remove(t);
			if (position.x > t.getX() && position.x <= t.getX() + Terrain.SIZE && position.z > t.getZ() && position.z <= t.getZ() + Terrain.SIZE) {
				return t;
			}
		}
		return null;
	}

	public void addLight(Light light) {
		handler.lights.add(light);
		if (handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(light));
		}
	}

}

class TTerrain {

	public Vector3f pos;
	private PerlinNoise generator;
	private float[] vertices, normals, textureCoords;
	private int[] indices;
	private float[][] heights;
	private int gridX, gridZ;
	int terrainId = 0;

	public TTerrain(int gridX, int gridZ, int terrainId) {
		this.gridX = gridX * Terrain.SIZE;
		this.gridZ = gridZ * Terrain.SIZE;
		pos = new Vector3f(gridX, 0, gridZ);
		this.terrainId = terrainId;
		generator = new PerlinNoise(gridX, gridZ, Terrain.VERTEX_COUNT, 75, 6, 0.34f, World.SEED);
		generateTerrain();
	}

	public Terrain createTerrain(Handler handler, Loader loader) {
		return new Terrain(gridX, gridZ, Assets.TERRAIN, heights, loader.loadToVAO(vertices, textureCoords, normals, indices), handler);
	}

	private void generateTerrain() {
		int VERTEX_COUNT = Terrain.VERTEX_COUNT;

		heights = new float[VERTEX_COUNT][VERTEX_COUNT];

		int count = VERTEX_COUNT * VERTEX_COUNT;
		vertices = new float[count * 3];
		normals = new float[count * 3];
		textureCoords = new float[count * 2];
		indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * Terrain.SIZE;
				float height = getHeight(j, i);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * Terrain.SIZE;
				Vector3f normal = calcNormal(j, i);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT + 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
	}

	/**
	 * Calculates normal from height generator
	 * 
	 * @param x
	 * @param z
	 * @param img
	 * @return
	 */
	private Vector3f calcNormal(int x, int z) {
		float heightL = getHeight(x - 1, z);
		float heightR = getHeight(x + 1, z);
		float heightD = getHeight(x, z - 1);
		float heightU = getHeight(x, z + 1);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	/**
	 * Generates height from height generator
	 * 
	 * @param x
	 * @param z
	 * @param generator
	 * @return
	 */
	public float getHeight(float x, float z) {
		return generator.generateHeight(x, z);
	}
}
