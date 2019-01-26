package com.countgandi.com.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.audio.AudioMaster;
import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.game.Constants;
import com.countgandi.com.game.Game;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.guis.WaterGui;

public class Camera {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.2f;
	private static final float FAR_PLANE = 1000;
	protected static final float runSpeed = 20, sprintSpeed = 100, JumpPower = 30;

	protected Matrix4f projectionMatrix;
	protected Matrix4f viewMatrix;
	protected Vector3f position = new Vector3f(0, 0, 0);
	protected float pitch, yaw, roll, height = 4;
	protected WaterGui waterGui;

	protected float currentSpeed;
	protected float velX;
	protected float upwardsSpeed;

	protected float TerrainHeight = 0;

	protected boolean isInAir = false, isInWater = false;
	protected int preX;

	protected Handler handler;

	public Camera(Handler handler) {
		this.handler = handler;
		waterGui = new WaterGui(new Vector2f(0, 0), new Vector2f(10, 10));
		projectionMatrix = createProjectionMatrix();
		viewMatrix = new Matrix4f();
	}

	public final void move() {

		Game.HEADER.setText("x:" + (int) position.getX() + " y:" + (int) position.getY() + " z:" + (int) position.z);
		AudioMaster.setListenerData(position.x, position.y, position.z);

		collision();
		checkInputs();

		updateViewMatrix();
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JumpPower;
			isInAir = true;
		}
	}

	private void collision() {
		TerrainHeight = handler.getWorld().getHeight(position);
		
		if (position.getY() <= handler.waters.get(0).getHeight() + 0.1f && !isInWater) {
			handler.guis.add(waterGui);
			isInWater = true;
		} else if (position.getY() > handler.waters.get(0).getHeight() + 0.1f && isInWater) {
			handler.guis.remove(waterGui);
			isInWater = false;
		}
	}

	protected void checkInputs() {
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
		
		if (Display.isActive()) {
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
		}

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

	public Vector3f getRot() {
		return new Vector3f(pitch, roll, yaw);
	}

	protected void updateViewMatrix() {
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	}

	private static Matrix4f createProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return this.viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

}
