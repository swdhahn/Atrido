package com.countgandi.com.game.biomes;

import com.countgandi.com.Assets;
import com.countgandi.com.renderEngine.terrain.Terrain;

public class BiomePlains extends Terrain {

	public BiomePlains(int gridX, int gridZ) {
		super(gridX, gridZ, Assets.PLAINS);
	}

}
