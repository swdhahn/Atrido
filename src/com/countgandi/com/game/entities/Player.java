package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Assets;
import com.countgandi.com.game.Handler;
import com.countgandi.com.renderEngine.OBJLoader;
import com.countgandi.com.renderEngine.models.TexturedModel;
import com.countgandi.com.renderEngine.textures.ModelTexture;

public class Player extends Entity {

	
	public Player(Vector3f position, Handler handler) {
		super(new TexturedModel(OBJLoader.loadObjModel("cube", Assets.loader), new ModelTexture(Assets.loader.loadTexture("TransparentSmallBlock"))), handler.getCamera().getPosition(), new Vector3f(handler.getCamera().getPitch(), handler.getCamera().getRoll(), handler.getCamera().getYaw()), handler);
		handler.getCamera().setPosition(position);
	}
	
	public void tick() { 
		this.setPosition(handler.getCamera().getPosition());
	}


}
