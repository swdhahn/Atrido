package com.countgandi.com.game.menus;

import com.countgandi.com.game.Handler;

public class GameMenu extends Menu {
	
	public GameMenu(Handler handler) {
		super(handler);
	}

	@Override
	public void tick() {
		handler.tick();
	}

	@Override
	public void render() {
		handler.render(false);
	}

}
