package com.countgandi.com.game.plants;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Assets;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.game.Handler;

public class PlantTree extends Plant {

	public PlantTree(Vector3f position, Vector3f rotation, Handler handler) {
		super(new TexturedModel[] {Assets.TexturedModels.tree, Assets.TexturedModels.treeleaves}, position, rotation, 1.0f, handler);
	}

}
