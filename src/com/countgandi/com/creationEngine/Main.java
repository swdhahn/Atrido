package com.countgandi.com.creationEngine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.creationEngine.menus.FoilageMenuSession;
import com.countgandi.com.creationEngine.menus.LandscapeMenuSession;
import com.countgandi.com.creationEngine.menus.SessionObject;
import com.countgandi.com.creationEngine.menus.SettingsMenuSession;
import com.countgandi.com.creationEngine.menus.WorldMenuSession;
import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.terrain.Terrain;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.Assets;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Light;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int WINDOW_WIDTH = 1920, WINDOW_HEIGHT = 1080;
	public static final int GL_WIDTH = 1100, GL_HEIGHT = 1040;
	
	public static DefaultListModel<SessionObject> worldList = new DefaultListModel<SessionObject>();

	public static boolean running = false;
	public static Loader loader;
	
	private Handler handler;

	public Main() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Atrido Game Engine");
		Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setPreferredSize(size);
		frame.setMaximumSize(size);
		frame.setMinimumSize(size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);
		addComponentsToJFrame(frame);
		frame.add(this);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		loader = new Loader();
		
		handler.getWorld().addLight(new Light(new Vector3f(0, 1000, -7000), new Vector3f(1.0f, 1.0f, 1.0f), Light.LIGHT_SUN));
		handler.getWorld().addWater(new WaterTile(0, 0, -1, WaterTile.SIZE));
		handler.getWorld().addTerrain(new Terrain(0, 0, Assets.TERRAIN, loader));

		start(frame);
	}
	
	public void renderGL() {
		handler.render(true);
	}

	public void start(JFrame frame) {
		if (running)
			return;
		running = true;
		
		while(!Display.isCloseRequested()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}
			renderGL();
			handler.getCamera().move();
			
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
		frame.dispose();
		running = false;
		System.exit(0);
		
	}

	public static void main(String[] args) {
		new Main();
	}

	private void addComponentsToJFrame(JFrame frame) {
		this.setSize(WINDOW_WIDTH - GL_WIDTH, GL_HEIGHT);
		this.setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.EAST);
		
		this.setBackground(Color.DARK_GRAY);
		tabbedPane.setBackground(Color.DARK_GRAY);
		tabbedPane.setForeground(Color.WHITE);
		
		Canvas canvas = new Canvas();
		frame.add(new JPanel().add(canvas));
		DisplayManager.addToCanvas(canvas, GL_WIDTH, GL_HEIGHT);
		
		this.handler = new Handler(true);
		
		tabbedPane.add(new WorldMenuSession(handler));
		tabbedPane.add(new LandscapeMenuSession(handler));
		tabbedPane.add(new FoilageMenuSession(handler));
		tabbedPane.add(new SettingsMenuSession(handler));
		
	}

}
