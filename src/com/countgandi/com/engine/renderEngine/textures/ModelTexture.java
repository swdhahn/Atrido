package com.countgandi.com.engine.renderEngine.textures;

public class ModelTexture {

	private Texture textureID;

	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean useFakeLighting = false;
	private boolean culling = true;

	private int numberOfRows = 1;

	public ModelTexture(Texture id, boolean culling) {
		this.textureID = id;
		this.culling = culling;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public Texture getID() {
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean hasCulling() {
		return culling;
	}

}
