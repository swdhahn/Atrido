package com.countgandi.com.menus;

import com.countgandi.com.game.Handler;

public class LoadingMenu extends Menu {

	public LoadingMenu(Handler handler) {
		super(handler);
	}

	@Override
	public void tick() {
		handler.tick();
	}

	@Override
	public void render() {
		handler.render();
	}

}
