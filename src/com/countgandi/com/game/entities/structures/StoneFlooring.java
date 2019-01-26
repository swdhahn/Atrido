package com.countgandi.com.game.entities.structures;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class StoneFlooring extends Structure {

	public StoneFlooring(Vector3f position, Handler handler) {
		super(Assets.stoneFlooringModel, position, new Vector3f(0, 0, 0), handler);
	}

}
