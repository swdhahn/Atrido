package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class TreeEntity extends Entity {

	

	public TreeEntity(Vector3f position, Vector3f rot, Handler handler) {
		super(Assets.TexturedModels.tree1, position, rot, handler);
	}

	@Override
	public void tick() {
		
	}
	
	public void render() {
		MasterRenderer.disableCulling();
	}
	
	public void endRender() {
		MasterRenderer.enableCulling();
	}

}
