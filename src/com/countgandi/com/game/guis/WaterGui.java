package com.countgandi.com.game.guis;

import org.lwjgl.util.vector.Vector2f;

import com.countgandi.com.engine.renderEngine.guis.GuiTexture;
import com.countgandi.com.game.Assets;

public class WaterGui extends GuiTexture {

	public WaterGui(Vector2f position, Vector2f scale) {
		super(Assets.Underwater, position, scale);
	}

}
