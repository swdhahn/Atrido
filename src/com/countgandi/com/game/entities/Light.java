package com.countgandi.com.game.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	public static final int LIGHT_DIRECTIONAL = 0;
	public static final int LIGHT_LAMP = 1;
	public static final int LIGHT_OTHER = 2;
	
	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	private int lightType;
	
	public Light(Vector3f position, Vector3f color, int lightType) {
		this.position = position;
		this.color = color;
		this.lightType = lightType;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation, int lightType) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.lightType = lightType;
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}
	
	public int getLightType() {
		return lightType;
	}

	public String toString() {
		if(lightType == LIGHT_DIRECTIONAL) {
			return "Directional Light";
		} else if(lightType == LIGHT_LAMP) {
			return "Lamp";
		} else if(lightType == LIGHT_OTHER) {
			return "none";
		}
		return "none";
	}

	public void move(Vector3f pos) {
		position.x += pos.x;
		position.y += pos.y;
		position.z += pos.z;
	}
	
	

}
