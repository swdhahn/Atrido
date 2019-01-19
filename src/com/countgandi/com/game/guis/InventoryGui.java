package com.countgandi.com.game.guis;

import org.lwjgl.util.vector.Vector2f;

import com.countgandi.com.engine.renderEngine.guis.GuiTexture;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;

public class InventoryGui extends Gui {
	
	private boolean isOpen = false;

	public InventoryGui(Handler handler) {
		super(handler);
		guis.add(new GuiTexture(Assets.Underwater, new Vector2f(1, 1), new Vector2f(1, 1)));
		
	}

	@Override
	public void tick() {
		
	}
	
	public void add() {
		this.handler.guis.addAll(guis);
		isOpen = true;
	}
	
	public void remove() {
		handler.guis.removeAll(guis);
		isOpen = false;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
