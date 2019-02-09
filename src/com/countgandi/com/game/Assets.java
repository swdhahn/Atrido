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
	private static final TerrainTexture GRASS = loadTerrainTexture("ngrassblended1080p");
	private static final TerrainTexture ROCK = loadTerrainTexture("rock");
	private static final TerrainTexture SAND = loadTerrainTexture("sand");
	private static final TerrainTexture SNOW = loadTerrainTexture("snow");
	private static final TerrainTexture REEF = loadTerrainTexture("reef");
	public static final TerrainTexturePack TERRAIN = new TerrainTexturePack(SAND, GRASS, SNOW, REEF, ROCK);

	// Textures
	public static final Texture Underwater = Texture.newTexture("underwater").create();
	
	public static final Texture pineTreeTex = Texture.newTexture("pineTree").normalMipMap().create();
	public static final Texture pineTreeSnowTex = Texture.newTexture("pineTreeSnow").normalMipMap().create();
	public static final Texture stoneTexture = Texture.newTexture("stoneTexture").normalMipMap().create();
	
	public static final Texture fernTexture = Texture.newTexture("fern").normalMipMap().create();

	// Textured Models
	public static final TexturedModel pineTreeModel = loadTexturedModel("PineTreeBranches", pineTreeTex, true);
	public static final TexturedModel pineTreeLeavesModel = loadTexturedModel("PineTreeLeaves", pineTreeTex, false);
	public static final TexturedModel pineTreeLeavesSnowModel = loadTexturedModel("PineTreeLeaves", pineTreeSnowTex, false);
	public static final TexturedModel tree1Model = loadTexturedModel("tree2", "mapleTree", true);
	public static final TexturedModel tree1leavesModel = loadTexturedModel("tree2leaves", "mapleTree", false);
	public static final TexturedModel tree1leavesSnowModel = loadTexturedModel("tree2leaves", "mapleTreeSnow", false);
	public static final TexturedModel shipModel = loadTexturedModel("Ship", "ShipTex", false);
	public static final TexturedModel hutModel = loadTexturedModel("hut", "thatch", false);
	public static final TexturedModel grassModel = loadTexturedModel("grass", "grassTexture", false);
	public static final TexturedModel fernModel = loadTexturedModel("fern", fernTexture, false);

	public static final TexturedModel stoneFlooringModel = loadTexturedModel("stoneFlooring", stoneTexture, true);
	public static final TexturedModel stoneRoofingModel = loadTexturedModel("stoneRoofing", stoneTexture, true);

	public static final int BounceSound = AudioMaster.loadSound("bounce");

	public static TerrainTexture loadTerrainTexture(String res) {
		return new TerrainTexture(Texture.newTexture("terrain/" + res).normalMipMap().create());
	}

	public static TexturedModel loadTexturedModel(String modelName, String textureName, boolean culling) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(Texture.newTexture(textureName).normalMipMap().create(), culling)) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}

	public static TexturedModel loadTexturedModel(String modelName, Texture tex, boolean culling) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(tex, culling)) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}

}
