package com.countgandi.com.game;

import com.countgandi.com.engine.audio.AudioMaster;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.OBJLoader;
import com.countgandi.com.engine.renderEngine.font.fontMeshCreator.FontType;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.textures.ModelTexture;
import com.countgandi.com.engine.renderEngine.textures.TerrainTexture;
import com.countgandi.com.engine.renderEngine.textures.TerrainTexturePack;
import com.countgandi.com.engine.renderEngine.textures.Texture;

public class Assets {

	public static final Loader loader = new Loader();

	// Fonts
	public static final class Fonts {
		public static final FontType arial = new FontType("arial", loader);
	}

	// Terrain Texture Packs
	private static final TerrainTexture GRASS = new TerrainTexture(Texture.newTexture("grass2").normalMipMap().create());
	//private static final TerrainTexture ROCK = new TerrainTexture(loader.loadTexture("rock"));
	private static final TerrainTexture SAND = new TerrainTexture(Texture.newTexture("sand").normalMipMap().create());
	//private static final TerrainTexture SNOW = new TerrainTexture(loader.loadTexture("snow"));
	public static final TerrainTexturePack TERRAIN = new TerrainTexturePack(SAND, GRASS, SAND, SAND);

	// Textures
	public static final Texture Underwater = Texture.newTexture("underwater").create();
	
	public static final Texture pineTreeTex = Texture.newTexture("pineTree").normalMipMap().create();
	public static final Texture pineTreeSnowTex = Texture.newTexture("pineTreeSnow").normalMipMap().create();

	// Textured Models
	public static final TexturedModel pineTreeModel = loadTexturedModel("PineTreeBranches", pineTreeTex, false);
	public static final TexturedModel pineTreeLeavesModel = loadTexturedModel("PineTreeLeaves", pineTreeTex, true);
	public static final TexturedModel pineTreeLeavesSnowModel = loadTexturedModel("PineTreeLeaves", pineTreeSnowTex, true);
	public static final TexturedModel tree1Model = loadTexturedModel("tree2", "mapleTree", false);
	public static final TexturedModel tree1leavesModel = loadTexturedModel("tree2leaves", "mapleTree", true);
	public static final TexturedModel tree1leavesSnowModel = loadTexturedModel("tree2leaves", "mapleTreeSnow", true);
	public static final TexturedModel shipModel = loadTexturedModel("Ship", "ShipTex", true);
	public static final TexturedModel hutModel = loadTexturedModel("hut", "thatch", true);
	public static final TexturedModel grassModel = loadTexturedModel("grass", "grassTexture", true);
	

	public static final int BounceSound = AudioMaster.loadSound("bounce");
	

	public static TexturedModel loadTexturedModel(String modelName, String textureName, boolean trans) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(Texture.newTexture(textureName).create(), trans)) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}
	
	public static TexturedModel loadTexturedModel(String modelName, Texture tex, boolean trans) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(tex, trans)) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}

}
