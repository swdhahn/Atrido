package com.countgandi.com.game.entities.plants;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Entity;

public class Plant extends Entity {

	public Plant(TexturedModel model, Vector3f position, Vector3f rot, float scale, Handler handler) {
		super(model, position, rot, scale, handler);
	}

}
