package com.countgandi.com.engine.renderEngine.plants;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.models.RawModel;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.shaders.StaticShader;
import com.countgandi.com.engine.renderEngine.textures.ModelTexture;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.plants.Plant;

public class PlantRenderer {

	private StaticShader shader;

	public PlantRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel[], List<Plant>> plants) {
		for (TexturedModel[] model : plants.keySet()) {
			for (int i = 0; i < model.length; i++) {
				prepareTexturedModel(model[i]);
				List<Plant> batch = plants.get(model);
				for (Plant plant : batch) {
					prepareInstance(plant);
					plant.render();
					GL11.glDrawElements(GL11.GL_TRIANGLES, model[i].getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT,
							0);
					plant.endRender();
				}
				unbindTexturedModel();
			}
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();

		shader.loadNumberOfRows(texture.getNumberOfRows());

		if (model.getTexture().isHasTransparency()) {
			MasterRenderer.disableCulling();
		}

		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariable(texture.getShineDamper(), texture.getReflectivity());

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Plant plant) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(plant.getPosition(), plant.getRotation().x,
				plant.getRotation().y, plant.getRotation().z, plant.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
