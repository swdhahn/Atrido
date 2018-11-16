package com.countgandi.com.game.worldGen;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.biomes.BiomeHills;
import com.countgandi.com.game.biomes.BiomeMountains;
import com.countgandi.com.game.biomes.BiomePlains;
import com.countgandi.com.renderEngine.MasterRenderer;
import com.countgandi.com.renderEngine.terrain.Terrain;
import com.countgandi.com.renderEngine.water.WaterTile;

public class World {

	public static int SEED = 0;
	private static Random random = new Random();
	public static ArrayList<Class<? extends Terrain>> existantTerrains = new ArrayList<Class<? extends Terrain>>();
	static {
		existantTerrains.add(BiomePlains.class);
		existantTerrains.add(BiomeHills.class);
		existantTerrains.add(BiomeMountains.class);
	}
	public ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	public ArrayList<Terrain> loadedTerrains = new ArrayList<Terrain>();

	public World() {

	}

	public void generateWorld() {
		SEED = new Random().nextInt(1000000000);
		random.setSeed(SEED);
		int terrainSize = (int) (WaterTile.TILE_SIZE / Terrain.SIZE);
		try {
			for (int x = 0; x < terrainSize; x++) {
				for (int z = 0; z < terrainSize; z++) {
					System.out.println("Creating terrain: x:" + x + " z:" + z);
					terrains.add((Terrain) existantTerrains.get(random.nextInt(existantTerrains.size())).getConstructors()[0].newInstance(x, z));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renderWorld(MasterRenderer renderer) {
		for (int i = 0; i < terrains.size(); i++) {
			renderer.processTerrain(terrains.get(i));
		}
	}

	public static Terrain getTerrainStandingOn(Vector3f position, World world) {
		for (int i = 0; i < world.terrains.size(); i++) {
			Terrain t = world.terrains.get(i);
			if (position.x >= t.getX() && position.x <= t.getX() + Terrain.SIZE) {
				if (position.z >= t.getZ() && position.z <= t.getZ() + Terrain.SIZE) {
					return t;
				}
			}
		}
		return null;
	}

}
