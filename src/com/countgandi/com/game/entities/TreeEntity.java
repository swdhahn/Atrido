package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Assets;
import com.countgandi.com.game.Handler;
import com.countgandi.com.renderEngine.MasterRenderer;

public class TreeEntity extends Entity {

	

	public TreeEntity(Vector3f position, Vector3f rot, Handler handler) {
		super(Assets.TexturedModels.tree, position, rot, handler);
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
