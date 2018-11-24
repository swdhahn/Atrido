package com.countgandi.com.game.worldGen;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Assets;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.terrain.Terrain;
import com.countgandi.com.game.biomes.Biome;

public class World {

	public static int SEED = 0;
	private static Random random = new Random();
	public static final int terrainSideAmount = 1000;

	public ArrayList<Terrain> terrains = new ArrayList<Terrain>();

	public World() {

	}

	public void generateWorld() {
		SEED = new Random().nextInt(1000000000);
		random.setSeed(SEED);
		try {
			int terrainWidth = (int) (terrainSideAmount / Terrain.SIZE);
			for (int x = 0; x < terrainWidth; x++) {
				for (int z = 0; z < terrainWidth; z++) {
					terrains.add(new Terrain(0, 0, Assets.TERRAIN));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renderWorld(MasterRenderer renderer) {
		for(Terrain t: terrains) {
			renderer.processTerrain(t);
		}
	}

	public float getHeight(Vector3f position) {
		if (position.x < 0 || position.z < 0 || position.x >= Terrain.SIZE || position.z >= Terrain.SIZE) {
			return 0;
		}
		return getTerrainStandingOn(position).getHeightOfTerrain(position.x, position.z);
	}

	public Terrain getTerrainStandingOn(Vector3f position) {
		for (int i = 0; i < terrains.size(); i++) {
			Terrain t = terrains.get(i);
			if(position.x > t.getX() && position.x < t.getX() + Terrain.SIZE && position.z > t.getZ() && position.z < t.getZ() + Terrain.SIZE) {
					return t;
			}
		}
		return null;
	}

}
