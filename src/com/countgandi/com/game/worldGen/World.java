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
import com.countgandi.com.game.Game;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;

public class World {

	public static int SEED = 0;
	public static Random random = new Random();
	public static final int terrainSideAmount = 2 * Terrain.SIZE;

	public ArrayList<Terrain> terrains = new ArrayList<Terrain>();

	private Handler handler;
	
	public World(Handler handler) {
		this.handler = handler;
	}

	public void generateWorld() {
		SEED = new Random().nextInt(1000000000);
		random.setSeed(SEED);
		Game.HEADER2.setText("Seed: " + SEED);
		try {
			int terrainWidth = (int) (terrainSideAmount / Terrain.SIZE) / 2;
			for (int x = -terrainWidth; x < terrainWidth; x++) {
				for (int z = -terrainWidth; z < terrainWidth; z++) {
					System.out.println("POS  x:" + x + " z:" + z);
					addTerrain(new Terrain(x, z, Assets.TERRAIN, Assets.loader));
					handler.waters.add(new WaterTile(x, z, -40, WaterTile.SIZE));
				}
			}

			for(int i = 0; i < terrainSideAmount; i++) {
				Vector3f pos = new Vector3f(random.nextFloat() * terrainSideAmount - terrainSideAmount / 2, 0, random.nextFloat() * terrainSideAmount - terrainSideAmount / 2);
				pos.y = getTerrainStandingOn(pos).getHeightOfTerrain(pos.x, pos.z);
				if(pos.y < 6)
					continue;
				//handler.addEntity(new Entity(Assets.TexturedModels.tree1, pos, new Vector3f(0, 0, 0), 10, handler) {});
				//handler.addEntity(new Entity(Assets.TexturedModels.tree1leaves, pos, new Vector3f(0, 0, 0), 10, handler) {});
			}
			
			for(int i = 0; i < terrainSideAmount * 10; i++) {
				Vector3f pos = new Vector3f(random.nextFloat() * terrainSideAmount - terrainSideAmount / 2, 0, random.nextFloat() * terrainSideAmount - terrainSideAmount / 2);
				pos.y = getTerrainStandingOn(pos).getHeightOfTerrain(pos.x, pos.z) - 1;
				if(pos.y < -6) continue;
				int scale = random.nextInt(10) + 5;
				int type = random.nextInt(10);
				if(type == 0) {
					if(pos.y > 99) {
						handler.addEntity(new Entity(Assets.pineTreeModel, pos, new Vector3f(0, 0, 0), scale, handler) {});
						handler.addEntity(new Entity(Assets.pineTreeLeavesSnowModel, pos, new Vector3f(0, 0, 0), scale, handler) {});
					} else {
						handler.addEntity(new Entity(Assets.pineTreeModel, pos, new Vector3f(0, 0, 0), scale, handler) {});
						handler.addEntity(new Entity(Assets.pineTreeLeavesModel, pos, new Vector3f(0, 0, 0), scale, handler) {});
					}
				} else if(type >= 1) {
					pos.y = pos.y + 1;
					//FoilageRenderer.addFoilage(new Foilage(Assets.fernModel, pos, new Vector3f(0, 0, 0), scale + 3f));
					handler.addEntity(new Entity(Assets.grassModel, pos, new Vector3f(0, 0, 0), 4, handler) {});
					pos.y = pos.y - 1;
				}
			}
			for(int i = 0; i < terrainSideAmount * 10; i++) {
				
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
		return getTerrainStandingOn(position).getHeightOfTerrain(position.x, position.z);
	}

	public Terrain getTerrainStandingOn(Vector3f position) {
		for (int i = 0; i < terrains.size(); i++) {
			Terrain t = terrains.get(i);
			if(position.x > t.getX() && position.x <= t.getX() + Terrain.SIZE && position.z > t.getZ() && position.z <= t.getZ() + Terrain.SIZE) {
				return t;
			}
		}
		return terrains.get(0);
	}

	public void addLight(Light light) {
		handler.lights.add(light);
		if(handler.getIsEngine()) {
			Main.worldList.addElement(new SessionObject(light));
		}
	}

}
