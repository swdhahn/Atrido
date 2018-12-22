package com.countgandi.com.engine.renderEngine.terrain;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.models.RawModel;
import com.countgandi.com.engine.renderEngine.textures.TerrainTexture;
import com.countgandi.com.engine.renderEngine.textures.TerrainTexturePack;
import com.countgandi.com.game.worldGen.World;

public class Terrain {

	// use a single terrain, but determine colors of map with heights and determine
	// biomes with heights

	public static final int SIZE = 1000, VERTEX_COUNT = SIZE / 10;
	private static final float MAX_HEIGHT = 30, MAX_PIXEL_COLOR = 256 * 256 * 256;

	private float x, z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private String heightmap;
	private PerlinNoise generator;

	private float[][] heights;

	public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, Loader loader) {
		this.texturePack = texturePack;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		generator = new PerlinNoise(gridX, gridZ, VERTEX_COUNT, World.SEED);
		this.model = generateTerrain(loader);
		blendMap = this.generateTextureMap(loader);
	}
	
	public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap, Loader loader) {
		this.texturePack = texturePack;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		generator = new PerlinNoise(gridX, gridZ, VERTEX_COUNT, World.SEED);
		this.model = generateTerrain(loader, heightMap);
		this.blendMap = blendMap;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX + 1 >= heights.length || gridZ + 1 >= heights.length || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;

		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	private RawModel generateTerrain(Loader loader) {

		heights = new float[VERTEX_COUNT][VERTEX_COUNT];

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calcNormal(j, i);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	/**
	 * Calculates normal from height generator
	 * 
	 * @param x
	 * @param z
	 * @param img
	 * @return
	 */
	private Vector3f calcNormal(int x, int z) {
		float heightL = getHeight(x - 1, z);
		float heightR = getHeight(x + 1, z);
		float heightD = getHeight(x, z - 1);
		float heightU = getHeight(x, z + 1);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	/**
	 * Generates height from height generator
	 * 
	 * @param x
	 * @param z
	 * @param generator
	 * @return
	 */
	public float getHeight(float x, float z) {
		return generator.generateHeight(x, z);
	}

	public String getHeightMap() {
		return heightmap;
	}

	private TerrainTexture generateTextureMap(Loader loader) {
		int width = SIZE, height = SIZE;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				int color = 0xFF000000;
				float theight = getHeightOfTerrain(x, z);
				if (theight <= 5) {
					color = 0xFFFF0000;
				}
				img.setRGB(x, z, color);
			}
		}
		return new TerrainTexture(loader.loadTexture(img));
	}
	
	private RawModel generateTerrain(Loader loader, String heightMap) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(Terrain.class.getResourceAsStream("/tex/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int VERTEX_COUNT = img.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, img);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calcNormal(j, i, img);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	private Vector3f calcNormal(int x, int z, BufferedImage img) {
		float heightL = getHeight(x - 1, z, img);
		float heightR = getHeight(x + 1, z, img);
		float heightD = getHeight(x, z - 1, img);
		float heightU = getHeight(x, z + 1, img);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, BufferedImage img) {
		if (x < 0 || x >= img.getHeight() || z < 0 || z >= img.getHeight()) {
			return 0;
		}
		float height = img.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

}
