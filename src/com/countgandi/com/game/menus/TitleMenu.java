package com.countgandi.com.game.menus;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.countgandi.com.engine.renderEngine.guis.GuiRenderer;
import com.countgandi.com.engine.renderEngine.guis.GuiTexture;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Game;
import com.countgandi.com.game.Handler;

public class TitleMenu extends Menu {

	private GuiRenderer renderer;
	private List<GuiTexture> textures = new ArrayList<GuiTexture>();
	public static float mx, my;
	
	public TitleMenu(Handler handler) {
		super(handler);
		renderer = new GuiRenderer(Assets.loader);
	}

	@Override
	public void tick() {
		mx = (float) Mouse.getX() / (float) Game.WIDTH;
		my = (float) Mouse.getY() / (float) Game.HEIGHT;
	}

	@Override
	public void render() {
		renderer.render(textures);
	}

}
