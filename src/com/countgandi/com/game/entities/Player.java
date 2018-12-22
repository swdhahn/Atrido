package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.OBJLoader;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.textures.ModelTexture;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class Player extends Entity {

	
	public Player(Vector3f position, Handler handler) {
		super(new TexturedModel(OBJLoader.loadObjModel("cube", Assets.loader), new ModelTexture(Assets.loader.loadTexture("TransparentSmallBlock"), false)), handler.getCamera().getPosition(), new Vector3f(handler.getCamera().getPitch(), handler.getCamera().getRoll(), handler.getCamera().getYaw()), handler);
		handler.getCamera().setPosition(position);
	}
	
	public void tick() { 
		this.setPosition(handler.getCamera().getPosition());
	}


}
