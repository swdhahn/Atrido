package com.countgandi.com.game.entities.structures;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Entity;

public class Structure extends Entity {

	public Structure(TexturedModel model, Vector3f position, Vector3f rot, Handler handler) {
		super(model, position, rot, 1, handler);
	}

}
