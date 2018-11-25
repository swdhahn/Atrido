package com.countgandi.com;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.audio.AudioMaster;
import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.engine.renderEngine.font.TextMaster;
import com.countgandi.com.engine.renderEngine.font.fontMeshCreator.GUIText;
import com.countgandi.com.engine.renderEngine.particles.ParticleMaster;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.GameRegistry;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.menus.GameMenu;
import com.countgandi.com.menus.Menu;

//https://youtu.be/mnIQEQoHHCU?t=7m6s
public class Game {

	public static final int WIDTH = 1600, HEIGHT = 900;
	public static final String TITLE = "-- OpenGL --";
	public static GUIText HEADER, HEADER2;

	public static Menu menu;

	private Handler handler;

	public Game() {
		// INIT
		DisplayManager.createDisplay();
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);

		handler = new Handler();
		HEADER = new GUIText("Pos", 1, Assets.Fonts.arial, new Vector2f(0, 0), 1);
		HEADER2 = new GUIText("Pos", 1, Assets.Fonts.arial, new Vector2f(0, 0.1f), 1);
		menu = new GameMenu(handler);

		GameRegistry.register();

		handler.lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(1.0f, 1.0f, 1.0f)));
		handler.waters.add(new WaterTile(WaterTile.TILE_SIZE, WaterTile.TILE_SIZE, -1));

		// END OF INIT
		handler.gameStart();
		run();
	}

	public void tick() {
		menu.tick();
	}
	
	private void render() {
		menu.render();
	}

	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				new Game();
			}
		}.start();
	}

	public void run() {

		while (!Display.isCloseRequested()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}
			tick();
			render();
			DisplayManager.updateDisplay();
		}

		System.out.println("Shutting Down Application");
		handler.cleanUp();
		Assets.loader.cleanUp();
		TextMaster.cleanUp();
		AudioMaster.cleanUp();
		ParticleMaster.cleanUp();
		DisplayManager.closeDisplay();
		System.exit(0);
	}

}

/*
 * private float[] vertices = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f,
 * -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
 * -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
 * 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
 * -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
 * 0.5f,
 * -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,
 * -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f,
 * 0.5f
 * };
 * 
 * private float[] textureCoords = {
 * 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
 * 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0
 * };
 * 
 * private int[] indicies = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11,
 * 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19, 17, 18, 20, 21, 23, 23, 21, 22
 * };
 */
