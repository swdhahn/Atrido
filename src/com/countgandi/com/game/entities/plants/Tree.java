package com.countgandi.com.game.entities.plants;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class Tree extends Plant {

	public Tree(Vector3f position, Vector3f rot, float scale, Handler handler) {
		super(Assets.pineTreeLeavesModel, position, rot, scale, handler);
	}

}
