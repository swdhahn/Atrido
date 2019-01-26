package com.countgandi.com.engine.renderEngine.foilage;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.engine.OpenGlUtils;
import com.countgandi.com.engine.renderEngine.models.RawModel;
import com.countgandi.com.engine.renderEngine.models.TexturedModel;
import com.countgandi.com.engine.renderEngine.shaders.InstancedStaticShader;
import com.countgandi.com.engine.renderEngine.textures.ModelTexture;
import com.countgandi.com.game.Assets;

public class FoilageRenderer {

	private static final int INSTANCE_DATA_LENGTH = 24;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(InstancedStaticShader.MAX_INSTANCED * INSTANCE_DATA_LENGTH);
	private static Map<TexturedModel, List<Foilage>> foilages = new HashMap<TexturedModel, List<Foilage>>();
	private InstancedStaticShader shader;
	private int pointer = 0;

	public FoilageRenderer(Matrix4f projectionMatrix, InstancedStaticShader shader) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public static void addFoilage(Foilage foilage) {
		TexturedModel foilageModel = foilage.model;
		List<Foilage> batch = foilages.get(foilageModel);
		
		if (batch != null && batch.size() < InstancedStaticShader.MAX_INSTANCED) {
			batch.add(foilage);
		} else {
			List<Foilage> newBatch = new ArrayList<Foilage>();
			if (!foilageModel.isInstanced) {
				foilageModel.vbo = Assets.loader.createEmptyVbo(InstancedStaticShader.MAX_INSTANCED * INSTANCE_DATA_LENGTH);
				Assets.loader.addInstancedAttribute(foilageModel.getRawModel().getVaoID(), foilageModel.vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
				Assets.loader.addInstancedAttribute(foilageModel.getRawModel().getVaoID(), foilageModel.vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
				Assets.loader.addInstancedAttribute(foilageModel.getRawModel().getVaoID(), foilageModel.vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
				Assets.loader.addInstancedAttribute(foilageModel.getRawModel().getVaoID(), foilageModel.vbo, 6, 4, INSTANCE_DATA_LENGTH, 20);
				foilageModel.isInstanced = true;
			}
			newBatch.add(foilage);
			foilages.put(foilageModel, newBatch);
		}
	}

	public void render() {
		for (TexturedModel model : foilages.keySet()) {
			prepareTexturedModel(model);
			List<Foilage> batch = foilages.get(model);
			
			float[] vboData = new float[batch.size() * INSTANCE_DATA_LENGTH];
			for (int i = 0; i < batch.size(); i++) {
				storeMatrixData(Maths.createTransformationMatrix(batch.get(i).pos, batch.get(i).rot.x, batch.get(i).rot.y, batch.get(i).rot.z, batch.get(i).scale), vboData);
			}
			Assets.loader.updateVbo(model.vbo, vboData, buffer);
			pointer = 0;

			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0, batch.size());
			unbindTexturedModel();
		}
	}

	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00;
		vboData[pointer++] = matrix.m01;
		vboData[pointer++] = matrix.m02;
		vboData[pointer++] = matrix.m03;
		vboData[pointer++] = matrix.m10;
		vboData[pointer++] = matrix.m11;
		vboData[pointer++] = matrix.m12;
		vboData[pointer++] = matrix.m13;
		vboData[pointer++] = matrix.m20;
		vboData[pointer++] = matrix.m21;
		vboData[pointer++] = matrix.m22;
		vboData[pointer++] = matrix.m23;
		vboData[pointer++] = matrix.m30;
		vboData[pointer++] = matrix.m31;
		vboData[pointer++] = matrix.m32;
		vboData[pointer++] = matrix.m33;
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		ModelTexture texture = model.getTexture();

		shader.loadNumberOfRows(texture.getNumberOfRows());

		if (texture.hasCulling()) {
			OpenGlUtils.cullBackFaces(true);
		}

		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariable(texture.getShineDamper(), texture.getReflectivity());

		model.getTexture().getID().bindToUnit(0);

	}

	private void unbindTexturedModel() {
		OpenGlUtils.cullBackFaces(false);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL30.glBindVertexArray(0);
	}

}
