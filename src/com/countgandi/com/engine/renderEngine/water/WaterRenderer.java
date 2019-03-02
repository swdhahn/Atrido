package com.countgandi.com.engine.renderEngine.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.models.RawModel;
import com.countgandi.com.engine.renderEngine.textures.Texture;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Light;

public class WaterRenderer {

	public static final Vector3f normalWater = new Vector3f(0.0f, 0.3f, 0.5f);
	public static final Vector3f darkWater = new Vector3f(-1, -0.8f, -1);
	public static final Vector3f dirtyWater = new Vector3f(0.5f, 0.1f, -0.2f);
	
	public static Vector3f waterColor = normalWater;

	private static final String DUDVMAP = "waterDUDV";
	private static final String NORMALMAP = "normalMap";
	private static final float WaveSpeed = 0.1f;

	private WaterShader shader;
	private WaterFrameBuffers fbos;

	private float moveFactor = 0;

	private RawModel model;
	private int dudvTexture;
	private int normalMap;

	public WaterRenderer(Camera camera, WaterFrameBuffers fbos, Loader loader) {
		this.shader = new WaterShader();
		this.fbos = fbos;
		dudvTexture = Texture.newTexture(DUDVMAP).anisotropic().create().textureId;
		normalMap = Texture.newTexture(NORMALMAP).anisotropic().create().textureId;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.stop();
		model = this.setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, waterColor, sun);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.SIZE);
			shader.loadTransformationMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		}
		unbind();
	}

	private void prepareRender(Camera camera, Vector3f waterColor, Light sun) {
		shader.start();
		shader.loadViewMatrix(camera.getViewMatrix());
		moveFactor += WaveSpeed * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun.getColor(), sun.getPosition());
		shader.loadWaterColor(waterColor);
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	private void unbind() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private RawModel setUpVAO(Loader loader) {
		// Just x and z vertex positions here, y is set to 0 in vshader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		return loader.loadToVAO(vertices, 2);
	}

}
