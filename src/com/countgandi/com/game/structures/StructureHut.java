package com.countgandi.com.game.structures;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class StructureHut extends Structure {

	public StructureHut(Vector3f position, Vector3f rotation, Handler handler) {
		super(Assets.TexturedModels.hut, position, rotation, 2.0f, handler);
	}

}
