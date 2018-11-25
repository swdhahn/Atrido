package com.countgandi.com.engine.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.countgandi.com.Game;

public class DisplayManager {
	private static final int FPSCAP = 120;

	private static long lastFrameTime;
	private static float delta;
	public static ContextAttribs attribs;

	public static void createDisplay() {

		attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			DisplayMode displayMode = null;
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == Game.WIDTH && modes[i].getHeight() == Game.HEIGHT && modes[i].isFullscreenCapable()) {
					displayMode = modes[i];
				}
			}
			Display.setDisplayMode(new DisplayMode(Game.WIDTH, Game.HEIGHT));
			// Display.setFullscreen(true);
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			Display.setTitle(Game.TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		try {
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Mouse.setClipMouseCoordinatesToWindow(true);
		GL11.glViewport(0, 0, Game.WIDTH, Game.HEIGHT);

	}

	private static int frameRate = 0; 
	private static float prev = 0;
	
	public static void updateDisplay() {
		frameRate ++;
		Mouse.updateCursor();
		Display.sync(FPSCAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
		if(delta - prev > 1) {
			prev = delta;
			System.out.println("FPS: " + frameRate);
			frameRate = 0;
		}
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}
