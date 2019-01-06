package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.OpenGlUtils;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class TreeEntity extends Entity {

	

	public TreeEntity(Vector3f position, Vector3f rot, Handler handler) {
		super(Assets.tree1Model, position, rot, handler);
	}

	@Override
	public void tick() {
		
	}
	
	public void render() {
		OpenGlUtils.cullBackFaces(false);
	}
	
	public void endRender() {
		OpenGlUtils.cullBackFaces(true);
	}

}
