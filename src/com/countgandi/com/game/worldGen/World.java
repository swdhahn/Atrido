package com.countgandi.com.game.worldGen;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;

/**
 * Issues with procedural generation?
 *  -memory leak
 *  -looping through too much and causing cpu usage overload
 * 
 * @author Count
 *
 */

public class World {

	public static int SEED = 0;
	public static Random random = new Random();
	private Thread thread;
	
	public static final int Visibility = 20;

	public CopyOnWriteArrayList<Entity> visibleEntities = new CopyOnWriteArrayList<Entity>();
	public CopyOnWriteArrayList<Terrain> renderterrains = new CopyOnWriteArrayList<Terrain>();
	public CopyOnWriteArrayList<Terrain> terrains = new CopyOnWriteArrayList<Terrain>();
	private CopyOnWriteArrayList<TTerrain> tempTerrains = new CopyOnWriteArrayList<TTerrain>();
	public boolean proceduralTerrainGenerating = false;

	private Handler handler;

	public World(Handler handler) {
		this.handler = handler;
		SEED = new Random().nextInt(2000000000);
	}

	public void generateWorld() {
		random.setSeed(SEED);
		Game.HEADER2.setText("Seed: " + SEED);
		try {
			handler.waters.add(new WaterTile(0, 0, 0));
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
			synchronized public void run() {
				long lastTime = System.nanoTime();
				double amountOfTicks = 5.0;
				double ns = 1000000000 / amountOfTicks;
				double delta = 0;
				while (proceduralTerrainGenerating) {
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
					while (delta >= 1) {
						if (!proceduralTerrainGenerating) {
							return;
						}
						for (int x = -Visibility; x < Visibility + 1; x++) {
							for (int z = -Visibility; z < Visibility + 1; z++) {
								Vector3f pos = new Vector3f(Math.round((handler.getCamera().getPosition().x / Terrain.SIZE)) * Terrain.SIZE + x * Terrain.SIZE, 0, Math.round((handler.getCamera().getPosition().z / Terrain.SIZE)) * Terrain.SIZE + z * Terrain.SIZE);
								boolean exists = terrainExists(pos);
								if (getTerrainStandingOn(pos) == null || !exists) {
									TTerrain t = new TTerrain((int) (pos.x / Terrain.SIZE), (int) (pos.z / Terrain.SIZE));
									tempTerrains.add(t);
								}
							}
						}

						for (Entity e : handler.entities) {
							boolean flag = true;
							for (Entity e2 : visibleEntities) {
								if (e.equals(e2)) {
									flag = false;
									if(!isVisible(e2.getPosition())) {
										visibleEntities.remove(e2);
									}
								}
							}
							if (flag && isVisible(e.getPosition())) {
								visibleEntities.add(e);
							}
						}
						
						for (Terrain t : terrains) {
							boolean flag = true;
							for (Terrain t2 : renderterrains) {
								if (t.equals(t2)) {
									flag = false;
									if(!isVisible(new Vector3f(t2.getX(), 0, t2.getZ()))) {
										renderterrains.remove(t2);
									}
								}
							}
							if (flag && isVisible(new Vector3f(t.getX(), 0, t.getZ()))) {
								renderterrains.add(t);
							}
						}
						delta--;
					}
				}
			}
		};
		thread.start();
	}

	private boolean isVisible(Vector3f pos) {
		if(pos.x > handler.getCamera().getPosition().getX() - Visibility * Terrain.SIZE && pos.x < handler.getCamera().getPosition().getX() + (Visibility + 1) * Terrain.SIZE) {
			if(pos.z > handler.getCamera().getPosition().getZ() - Visibility * Terrain.SIZE && pos.z < handler.getCamera().getPosition().getZ() + (Visibility + 1) * Terrain.SIZE) {
				return true;
			}
		}
		return false;
	}

	private boolean terrainExists(Vector3f pos) {
		for (Terrain t : terrains) {
			if (t.getX() == pos.x && t.getZ() == pos.z) {
				return true;
			}
		}
		for (TTerrain t : tempTerrains) {
			if (t.pos.x == pos.x && t.pos.z == pos.z) {
				return true;
			}
		}
		return false;
	}

	public void updateTerrain() {
		for (TTerrain t : tempTerrains) {
			Terrain tt = t.createTerrain(handler, Assets.loader);
			terrains.add(tt);
			tt.decorateTerrain(handler);
		}

		tempTerrains.clear();

		for (Terrain t : renderterrains) {
			if(!isVisible(new Vector3f(t.getX(), 0, t.getZ()))) {
				renderterrains.remove(t);
			}
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
		for (Terrain t : renderterrains) {
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
			if (t == null || position == null)
				terrains.remove(t);
			if (position.x >= t.getX() && position.x <= t.getX() + Terrain.SIZE) {
				if (position.z >= t.getZ() && position.z <= t.getZ() + Terrain.SIZE) {
					return t;
				}
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

	public TTerrain(int gridX, int gridZ) {
		this.gridX = gridX * Terrain.SIZE;
		this.gridZ = gridZ * Terrain.SIZE;
		pos = new Vector3f(gridX, 0, gridZ);
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
