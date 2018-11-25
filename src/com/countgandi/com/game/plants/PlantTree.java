package com.countgandi.com.game.plants;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Assets;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.game.Handler;

public class PlantTree extends Plant {

	public PlantTree(Vector3f position, Vector3f rotation, Handler handler) {
		super(new TexturedModel[] {Assets.TexturedModels.tree1}, position, rotation, 4.0f, handler);
	}

}
