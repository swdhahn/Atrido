package com.countgandi.com.engine.renderEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.renderEngine.DisplayManager;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.models.RawModel;
import com.countgandi.com.game.Game;
import com.countgandi.com.game.Handler;
import com.countgandi.com.game.entities.Camera;

public class SkyboxRenderer {
	
	private static final float SIZE = 2000f, height = 10f;
	
	private static final float[] vertices = new float[] {
			-SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE, -SIZE,
	};
	
	private static String[] TextureFiles = {"skybox/right", "skybox/left", "skybox/top", "skybox/bottom", "skybox/back", "skybox/front"};
	private static String[] NightTextureFiles = {"skybox/nightRight", "skybox/nightLeft", "skybox/nightTop", "skybox/nightBottom", "skybox/nightBack", "skybox/nightFront"};
	
	private RawModel quad;
	private int texture, nightTexture;
	private SkyboxShader shader;
	public static float time = 8000.0f;
	private Handler handler;
	
	public SkyboxRenderer(Matrix4f projectionMatrix, Handler handler, Loader loader) {
		this.handler = handler;
		quad = loader.loadToVAO(vertices, 3);
		texture = loader.loadCubeMap(TextureFiles);
		nightTexture = loader.loadCubeMap(NightTextureFiles);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, Vector3f fogColor) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(fogColor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
		time += DisplayManager.getFrameTimeSeconds() * 10;
		time %= 24000;
		
		handler.lights.get(0).getPosition().y = (float) (Math.sin(time / 6000.0f - 0.5f) * 10000);
		float blendFactor = handler.lights.get(0).getPosition().y / 10000;

		if(blendFactor < 0) blendFactor = 0;
		if(blendFactor > 1) blendFactor = 1;
		
		MasterRenderer.skyColor.x = MasterRenderer.normalSky.x * blendFactor;
		MasterRenderer.skyColor.y = MasterRenderer.normalSky.y * blendFactor;
		MasterRenderer.skyColor.z = MasterRenderer.normalSky.z * blendFactor;
		handler.lights.get(0).getColor().x = blendFactor;
		handler.lights.get(0).getColor().y = blendFactor;
		handler.lights.get(0).getColor().z = blendFactor;
		handler.lights.get(0).getPosition().x = handler.getCamera().getPosition().x + time * 2 - 24000;
		handler.lights.get(0).getPosition().z = handler.getCamera().getPosition().z + time * 2 - 24000;
		
		shader.loadBlendFactor(1 - blendFactor);
		Game.HEADER3.setText("Time: " + (int)(time));
	}

}
