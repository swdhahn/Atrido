package com.countgandi.com.creationEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Camera;

public class IndependentCamera extends Camera {
	
	private static int MAX_DOWNSPEED = 20;
	private static int XZ_SPEED = 20;
	private int downSpeed = 0;

	public IndependentCamera(Handler handler) {
		super(handler);
	}

	public void move() {

		if (Mouse.isButtonDown(2)) {
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				MAX_DOWNSPEED = 100;
				XZ_SPEED = 100;
			} else {
				MAX_DOWNSPEED = 20;
				XZ_SPEED = 20;
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.currentSpeed = -XZ_SPEED;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.currentSpeed = XZ_SPEED;
			} else {
				currentSpeed = 0;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				this.velX = XZ_SPEED;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				this.velX = -XZ_SPEED;
			} else {
				this.velX = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				downSpeed = -MAX_DOWNSPEED;
			} else if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				downSpeed = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				downSpeed = MAX_DOWNSPEED;
			} else if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				downSpeed = 0;
			}

			pitch -= Mouse.getDY();
			yaw += Mouse.getDX();

			float dy = (float)(downSpeed * DisplayManager.getFrameTimeSeconds());
			float dx = (float) ((currentSpeed * DisplayManager.getFrameTimeSeconds()) * Math.sin(Math.toRadians(-yaw)));
			float dz = (float) ((currentSpeed * DisplayManager.getFrameTimeSeconds()) * Math.cos(Math.toRadians(-yaw)));
			float dx2 = (float) ((velX * DisplayManager.getFrameTimeSeconds()) * Math.cos(Math.toRadians(yaw)));
			float dz2 = (float) ((velX * DisplayManager.getFrameTimeSeconds()) * Math.sin(Math.toRadians(yaw)));

			position.x += dx + dx2;
			position.z += dz + dz2;
			position.y += dy;
		}

	}

}
