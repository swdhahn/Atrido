package com.countgandi.com.game.menus;

import com.countgandi.com.game.Handler;

public abstract class Menu {
	
	protected Handler handler;
	
	public Menu(Handler handler) {
		this.handler = handler;
	}
	
	public abstract void tick();
	public abstract void render();

}
