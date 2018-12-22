package com.countgandi.com.game.worldGen;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.creationEngine.Main;
import com.countgandi.com.creationEngine.menus.SessionObject;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.terrain.Terrain;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Light;

public class World {

	public static int SEED = 0;
	private static Random random = new Random();
	public static final int terrainSideAmount = 1000;

	public ArrayList<Terrain> terrains = new ArrayList<Terrain>();

	private Handler handler;
	
	public World(Handler handler) {
		this.handler = handler;
	}

	public void generateWorld() {
		SEED = new Random().nextInt(1000000000);
		random.setSeed(SEED);
		try {
			int terrainWidth = (int) (terrainSideAmount / Terrain.SIZE);
			for (int x = 0; x < terrainWidth; x++) {
				for (int z = 0; z < terrainWidth; z++) {
					terrains.add(new Terrain(0, 0, Assets.TERRAIN, Assets.loader));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
		if(handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(terrain));
		}
	}
		
	public void addWater(WaterTile waterTile) {
		handler.waters.add(waterTile);
		if(handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(waterTile));
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

	public void addLight(Light light) {
		handler.lights.add(light);
		if(handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(light));
		}
	}

}
