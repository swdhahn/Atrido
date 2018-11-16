package com.countgandi.com.renderEngine.terrain;

import java.awt.image.BufferedImage;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.Assets;
import com.countgandi.com.game.worldGen.World;
import com.countgandi.com.renderEngine.Loader;
import com.countgandi.com.renderEngine.models.RawModel;
import com.countgandi.com.renderEngine.textures.TerrainTexture;
import com.countgandi.com.renderEngine.textures.TerrainTexturePack;
import com.countgandi.com.toolbox.Maths;

public abstract class Terrain {

	public static final float SIZE = 1000;
	public static final int VERTEX_COUNT = 100;

	private float x, z, multiple;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private String heightmap;
	private PerlinNoise generator;

	private float[][] heights;

	public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, float multiple) {
		this.multiple = multiple;
		this.texturePack = texturePack;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		generator = new PerlinNoise(gridX, gridZ, VERTEX_COUNT, 100f, 5f, 0.3f,  World.SEED);
		this.model = generateTerrain(Assets.loader);
		this.blendMap = this.generateBlendMap();
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

	public TerrainTexture getBlendMap() {
		return blendMap;
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
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
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
				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calcNormal(j, i, generator);
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
	private Vector3f calcNormal(int x, int z, HeightsGenerator generator) {
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightD = getHeight(x, z - 1, generator);
		float heightU = getHeight(x, z + 1, generator);
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
	private final float getHeight(int x, int z, HeightsGenerator generator) {
		return generator.generateHeight(x, z, multiple);
	}

	public String getHeightMap() {
		return heightmap;
	}

	private TerrainTexture generateBlendMap() {
		BufferedImage img = new BufferedImage(VERTEX_COUNT, VERTEX_COUNT, BufferedImage.TYPE_4BYTE_ABGR);
		for (int z = 0; z < img.getHeight(); z++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int color = 0xFF000000;
				if (heights[x][z] <= 10) {
					color = 0xFFFF0000;
				}
				img.setRGB(x, z, color);
			}
		}
		return new TerrainTexture(Assets.loader.loadTexture(img));
	}

}
