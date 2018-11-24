package com.countgandi.com.engine.renderEngine.terrain;

public enum TerrainID {

	Overworld("Overworld"), Savannah("Savannah"), Ocean("Ocean");
	
	private String id;
	
	private TerrainID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
	
}
