package com.countgandi.com.game.menus;

import com.countgandi.com.game.Handler;
import com.countgandi.com.net.client.Client;
import com.countgandi.com.net.server.Server;

public class GameMenu extends Menu {
	
	private Client client;
	
	public GameMenu(Handler handler) {
		super(handler);
		client = new Client(Server.Port, "localhost", "hello", handler);
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
