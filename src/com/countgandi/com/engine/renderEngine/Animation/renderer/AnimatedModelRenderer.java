package com.countgandi.com.engine.renderEngine.Animation.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.engine.OpenGlUtils;
import com.countgandi.com.engine.renderEngine.Animation.animatedModel.AnimatedModel;
import com.countgandi.com.game.entities.Camera;

public class AnimatedModelRenderer {

	private AnimatedModelShader shader;

	/**
	 * Initializes the shader program used for rendering animated models.
	 */
	public AnimatedModelRenderer() {
		this.shader = new AnimatedModelShader();
	}

	/**
	 * Renders an animated entity. The main thing to note here is that all the
	 * joint transforms are loaded up to the shader to a uniform array. Also 5
	 * attributes of the VAO are enabled before rendering, to include joint
	 * indices and weights.
	 * 
	 * @param entity
	 *            - the animated entity to be rendered.
	 * @param camera
	 *            - the camera used to render the entity.
	 * @param lightDir
	 *            - the direction of the light in the scene.
	 */
	public void render(AnimatedModel model, Camera camera, Vector3f lightDir) {
		prepare(camera, lightDir);
		model.getTexture().bindToUnit(0);
		model.getModel().bind(0, 1, 2, 3, 4);
		shader.loadJointTransformations(model.getJointTransforms());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		model.getModel().unbind(0, 1, 2, 3, 4);
		finish();
	}

	/**
	 * Deletes the shader program when the game closes.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

	/**
	 * Starts the shader program and loads up the projection view matrix, as
	 * well as the light direction. Enables and disables a few settings which
	 * should be pretty self-explanatory.
	 * 
	 * @param camera
	 *            - the camera being used.
	 * @param lightDir
	 *            - the direction of the light in the scene.
	 */
	private void prepare(Camera camera, Vector3f lightDir) {
		shader.start();
		shader.loadProjectionViewMatrix(Matrix4f.mul(camera.getProjectionMatrix(), camera.getViewMatrix(), null));
		shader.loadLightDirection(lightDir);
		OpenGlUtils.antialias(true);
		OpenGlUtils.disableBlending();
		OpenGlUtils.enableDepthTesting(true);
	}

	/**
	 * Stops the shader program after rendering the entity.
	 */
	private void finish() {
		shader.stop();
	}

}
