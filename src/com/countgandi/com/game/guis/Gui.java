package com.countgandi.com.game.guis;

import java.util.ArrayList;

import com.countgandi.com.engine.renderEngine.guis.GuiTexture;
import com.countgandi.com.game.Handler;

public abstract class Gui {
	
	public ArrayList<GuiTexture> guis = new ArrayList<GuiTexture>();
	protected Handler handler;
	
	public Gui(Handler handler) {
		this.handler = handler;
	}
	
	public abstract void tick();

}
