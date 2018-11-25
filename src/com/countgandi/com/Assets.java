package com.countgandi.com;

import com.countgandi.com.engine.audio.AudioMaster;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.OBJLoader;
import com.countgandi.com.engine.renderEngine.font.fontMeshCreator.FontType;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.textures.ModelTexture;
import com.countgandi.com.engine.renderEngine.textures.TerrainTexture;
import com.countgandi.com.engine.renderEngine.textures.TerrainTexturePack;

public class Assets {

	public static final Loader loader = new Loader();

	// Fonts
	public static final class Fonts {
		public static final FontType arial = new FontType("arial", loader);
	}

	// Terrain Texture Packs
	private static final TerrainTexture GRASS = new TerrainTexture(loader.loadTexture("grass2"));
	private static final TerrainTexture ROCK = new TerrainTexture(loader.loadTexture("rock"));
	private static final TerrainTexture SAND = new TerrainTexture(loader.loadTexture("sand"));
	private static final TerrainTexture MUD = new TerrainTexture(loader.loadTexture("mud"));
	public static final TerrainTexturePack TERRAIN = new TerrainTexturePack(GRASS, SAND, ROCK, MUD);

	// Textures
	public static final int Underwater = loader.loadTexture("underwater");

	// Textured Models
	public static final class TexturedModels {
		public static final TexturedModel tree1 = loadTexturedModel("tree", "mapleTree", false);
		//public static final TexturedModel tree1leaves = loadTexturedModel("tree2leaves", "mapleTree", true);
		public static final TexturedModel hut = loadTexturedModel("hut", "thatch", true);
	}
	
	public static final class Sounds {
		public static final int Bounce = AudioMaster.loadSound("bounce");
	}

	public static TexturedModel loadTexturedModel(String modelName, String textureName, boolean trans) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName), new ModelTexture(loader.loadTexture(textureName), trans)) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}
	
	public static TexturedModel loadTexturedModel(String modelName, int texture, boolean trans) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName), new ModelTexture(texture, trans)) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}

}
