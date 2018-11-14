package com.countgandi.com.renderEngine.particles;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.Constants;
import com.countgandi.com.renderEngine.DisplayManager;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	private float elapsedTime;
	
	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
		this.position = new Vector3f(position.x, position.y, position.z);
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	protected boolean tick() {
		velocity.y += Constants.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}

}
