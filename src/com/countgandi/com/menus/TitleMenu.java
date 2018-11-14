package com.countgandi.com.menus;

import java.util.ArrayList;
import java.util.List;

import com.countgandi.com.game.Handler;
import com.countgandi.com.guis.GuiRenderer;
import com.countgandi.com.guis.GuiTexture;

public class TitleMenu extends Menu {
	
	private GuiRenderer renderer;
	private List<GuiTexture> textures = new ArrayList<GuiTexture>();

	public TitleMenu(Handler handler) {
		super(handler);
		renderer = new GuiRenderer();
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render() {
		renderer.render(textures);
	}

}
