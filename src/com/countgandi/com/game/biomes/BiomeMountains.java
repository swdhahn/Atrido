package com.countgandi.com.game.biomes;

import com.countgandi.com.Assets;
import com.countgandi.com.renderEngine.terrain.Terrain;

public class BiomeMountains extends Terrain {

	public BiomeMountains(int gridX, int gridZ) {
		super(gridX, gridZ, Assets.MOUNTAINS);
	}

}
