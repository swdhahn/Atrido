package com.countgandi.com.game.biomes;

import com.countgandi.com.Assets;
import com.countgandi.com.renderEngine.terrain.Terrain;

public class BiomeHills extends Terrain {

	public BiomeHills(int gridX, int gridZ) {
		super(gridX, gridZ, Assets.HILLS, 10.6f);
	}

}
