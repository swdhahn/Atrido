package com.countgandi.com.game.entities.structures;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.OpenGlUtils;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.shaders.StaticShader;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;

public class Structure extends Entity {
	
	protected boolean beingPlace = false, isWireFrame = false;

	public Structure(TexturedModel model, Vector3f position, Vector3f rot, Handler handler) {
		super(model, position, rot, 10, handler);
		Light light = new Light(position, new Vector3f(0, 0.5f, 1), Light.LIGHT_LAMP);
		light.setAttenuation(new Vector3f(0, 0, 1));
		handler.lights.add(light);
	}
	
	@Override
	public void render(StaticShader shader) {
		isWireFrame = OpenGlUtils.isWireFrame();
		OpenGlUtils.goWireframe(beingPlace);
	}

	@Override
	public void endRender(StaticShader shader) {
		OpenGlUtils.goWireframe(isWireFrame);
	}

}
