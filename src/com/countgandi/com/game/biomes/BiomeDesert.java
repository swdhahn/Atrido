package com.countgandi.com.game.biomes;

import com.countgandi.com.Assets;
import com.countgandi.com.renderEngine.terrain.Terrain;

public class BiomeDesert extends Terrain {

	public BiomeDesert(int gridX, int gridZ) {
		super(gridX, gridZ, Assets.DESERT);
	}

}
