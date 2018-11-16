package com.countgandi.com.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Game;
import com.countgandi.com.game.Constants;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.worldGen.World;
import com.countgandi.com.guis.WaterGui;
import com.countgandi.com.renderEngine.DisplayManager;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch, yaw, roll, height = 4;
	private WaterGui waterGui;

	private static final float runSpeed = 20, sprintSpeed = 100, JumpPower = 30;

	private float currentSpeed;
	private float velX;
	private float upwardsSpeed;

	private float TerrainHeight = 0;

	private boolean isInAir = false, isInWater = false;
	private int preX;

	private Handler handler;

	public Camera(Handler handler) {
		this.handler = handler;
		waterGui = new WaterGui(new Vector2f(0, 0), new Vector2f(10, 10));
	}

	public void move() {

		Game.HEADER.setText("x:" + (int) position.getX() + " y:" + (int) position.getY() + " z:" + (int) position.z);
		Game.HEADER2.setText(World.getTerrainStandingOn(position, handler.getWorld()).getClass().getName().replaceAll("Biome", "Biome ").substring("com.countgandi.com.game.biomes.".length()));
		if (position.getY() <= handler.waters.get(0).getHeight() + 0.1f && !isInWater) {
			handler.guis.add(waterGui);
			isInWater = true;
		} else if (position.getY() > handler.waters.get(0).getHeight() + 0.1f && isInWater) {
			handler.guis.remove(waterGui);
			isInWater = false;
		}

		collision();
		checkInputs();

		if (Mouse.getX() >= Game.WIDTH - 20) {
			Mouse.setCursorPosition(30, Mouse.getY());
			preX = Mouse.getX();
		}
		if (Mouse.getX() <= 20) {
			Mouse.setCursorPosition(Game.WIDTH - 30, Mouse.getY());
			preX = Mouse.getX();
		}

		pitch = ((Display.getHeight() / 2) - Mouse.getY()) / 5;
		yaw -= (float) (Math.toRadians(preX - Mouse.getX()) * 2) * ((float) Game.WIDTH / 180.0F);
		preX = Mouse.getX();

		float dx = (float) ((currentSpeed * DisplayManager.getFrameTimeSeconds()) * Math.sin(Math.toRadians(-yaw)));
		float dz = (float) ((currentSpeed * DisplayManager.getFrameTimeSeconds()) * Math.cos(Math.toRadians(-yaw)));
		float dx2 = (float) ((velX * DisplayManager.getFrameTimeSeconds()) * Math.cos(Math.toRadians(yaw)));
		float dz2 = (float) ((velX * DisplayManager.getFrameTimeSeconds()) * Math.sin(Math.toRadians(yaw)));

		position.x += dx + dx2;
		position.z += dz + dz2;

		upwardsSpeed += Constants.GRAVITY * DisplayManager.getFrameTimeSeconds();
		position.y += upwardsSpeed * DisplayManager.getFrameTimeSeconds();

		if (position.y - height < TerrainHeight) {
			upwardsSpeed = 0;
			position.y = TerrainHeight + height;
			isInAir = false;
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JumpPower;
			isInAir = true;
		}
	}

	private void collision() {
		TerrainHeight = World.getTerrainStandingOn(getPosition(), handler.getWorld()).getHeightOfTerrain(position.x, position.z);
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = -runSpeed;
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				this.currentSpeed = -sprintSpeed;
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = runSpeed;
		} else {
			currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.velX = runSpeed;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.velX = -runSpeed;
		} else {
			this.velX = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}

	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public void invertPitch() {
		this.pitch = -pitch;
	}

}
