package com.countgandi.com;

import com.countgandi.com.renderEngine.Loader;
import com.countgandi.com.renderEngine.OBJLoader;
import com.countgandi.com.renderEngine.font.fontMeshCreator.FontType;
import com.countgandi.com.renderEngine.models.TexturedModel;
import com.countgandi.com.renderEngine.textures.ModelTexture;
import com.countgandi.com.renderEngine.textures.TerrainTexture;
import com.countgandi.com.renderEngine.textures.TerrainTexturePack;

public class Assets {

	public static final Loader loader = new Loader();

	// Fonts
	public static final class Fonts {
		public static final FontType arial = new FontType("arial", loader);
	}

	// Terrain Texture Packs
	private static final TerrainTexture GRASS = new TerrainTexture(loader.loadTexture("grassy2"));
	private static final TerrainTexture ROCK = new TerrainTexture(loader.loadTexture("rock"));
	private static final TerrainTexture SAND = new TerrainTexture(loader.loadTexture("sand"));
	private static final TerrainTexture MUD = new TerrainTexture(loader.loadTexture("mud"));
	public static final TerrainTexturePack PLAINS = new TerrainTexturePack(GRASS, SAND, ROCK, MUD);
	public static final TerrainTexturePack HILLS = new TerrainTexturePack(GRASS, GRASS, ROCK, MUD);
	public static final TerrainTexturePack MOUNTAINS = new TerrainTexturePack(GRASS, GRASS, ROCK, MUD);

	// Textures
	public static final int Underwater = loader.loadTexture("underwater");

	// Textured Models
	public static final class TexturedModels {
		public static final TexturedModel tree = loadTexturedModel("NewTree78", "treeTextured");

	}

	public static TexturedModel loadTexturedModel(String modelName, String textureName) {
		return new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(loader.loadTexture(textureName))) {
			@Override
			public ModelTexture getTexture() {
				return super.getTexture();
			}
		};
	}

}
