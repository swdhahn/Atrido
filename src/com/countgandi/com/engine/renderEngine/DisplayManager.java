package com.countgandi.com.engine.renderEngine;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	private static final int FPSCAP = 120;

	private static long lastFrameTime;
	private static float delta;
	public static ContextAttribs attribs;

	public static void createDisplay(int width, int height, String title) {
		attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

		try {
			//DisplayMode displayMode = null;
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == width && modes[i].getHeight() == height && modes[i].isFullscreenCapable()) {
					//displayMode = modes[i];
				}
			}
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setFullscreen(true);
			Display.create(new PixelFormat().withDepthBits(24).withSamples(8), attribs);
			Display.setTitle(title);
			//Display.setLocation(1920, 0);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		try {
			Mouse.create();
			Keyboard.create();
	        Keyboard.enableRepeatEvents(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Mouse.setClipMouseCoordinatesToWindow(true);
		GL11.glViewport(0, 0, width, height);

	}
	
	public static Canvas addToCanvas(Canvas canvas, int width, int height) {
		attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setParent(canvas);
			Display.create(new PixelFormat().withDepthBits(24), attribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		try {
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Mouse.setClipMouseCoordinatesToWindow(true);
		GL11.glViewport(0, 0, width, height);
		canvas.setSize(width, height);
		return canvas;
	}

	private static float prev = 0;
	
	public static void updateDisplay() {
		Mouse.updateCursor();
		Display.sync(FPSCAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
		if(delta - prev > 1) {
			prev = delta;
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
