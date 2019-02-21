package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class MPlayer extends Entity {
	
	public String username;

	public MPlayer(Vector3f position, Handler handler) {
		super(Assets.shipModel, position, new Vector3f(0, 0, 0), handler);
	}

}
