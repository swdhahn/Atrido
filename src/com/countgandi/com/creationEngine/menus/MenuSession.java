package com.countgandi.com.creationEngine.menus;

import java.awt.Color;

import javax.swing.JPanel;

import com.countgandi.com.game.Handler;

public abstract class MenuSession extends JPanel {
	private static final long serialVersionUID = 1L;
	public static boolean worldUpdated = false;
	
	protected String title;
	protected Handler handler;

	public MenuSession(String title, Handler handler) {
		this.handler = handler;
		this.title = title;
		this.setBackground(Color.DARK_GRAY);
		this.setForeground(Color.WHITE);
	}
	
	@Override
	public String getName() {
		return title;
	}

}
