package com.countgandi.com.game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.game.entities.Player;
import com.countgandi.com.game.worldGen.World;
import com.countgandi.com.guis.GuiRenderer;
import com.countgandi.com.guis.GuiTexture;
import com.countgandi.com.renderEngine.MasterRenderer;
import com.countgandi.com.renderEngine.font.TextMaster;
import com.countgandi.com.renderEngine.particles.Particle;
import com.countgandi.com.renderEngine.particles.ParticleMaster;
import com.countgandi.com.renderEngine.water.WaterFrameBuffers;
import com.countgandi.com.renderEngine.water.WaterRenderer;
import com.countgandi.com.renderEngine.water.WaterTile;

public class Handler {
	
	public ArrayList<Entity> entities = new ArrayList<Entity> ();
	public ArrayList<GuiTexture> guis = new ArrayList<GuiTexture>();
	public ArrayList<Light> lights = new ArrayList<Light>();
	public ArrayList<WaterTile> waters = new ArrayList<WaterTile>();
	
	private WaterRenderer waterRenderer;
	private WaterFrameBuffers fbos;
	private MasterRenderer renderer;
	private GuiRenderer guiRenderer;
	private Camera camera;
	
	private Player player;
	private World world;
	
	public Handler () {
		renderer = new MasterRenderer();
		guiRenderer = new GuiRenderer();
		camera = new Camera(this);

		ParticleMaster.init(renderer.getProjectionMatrix());
		TextMaster.init();
		fbos = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(renderer.getProjectionMatrix(), fbos);
		world = new World();
	}
	
	public void tick() {
		camera.move();
		lights.get(0).setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y + 200, camera.getPosition().z - 100));
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		new Particle(new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z - 10), new Vector3f(0, 30, 0), 0.1F, 100, 0, 10);
		ParticleMaster.tick();
	}
	
	public void render() {
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

		fbos.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderScene(new Vector4f(0, 1, 0, -waters.get(0).getHeight() + 1));
		camera.getPosition().y += distance;
		camera.invertPitch();

		fbos.bindRefractionFrameBuffer();
		renderScene(new Vector4f(0, -1, 0, waters.get(0).getHeight() + 1));

		fbos.unbindCurrentFrameBuffer();

		renderScene(new Vector4f(0, -1, 0, 10000));
		waterRenderer.render(waters, camera, lights.get(0));

		ParticleMaster.renderParticles(camera);

		// 2D stuff
		guiRenderer.render(guis);
		TextMaster.render();
	}

	private void renderScene(Vector4f clipPlane) {
		for(int i = 0; i < entities.size(); i++) {
			renderer.processEntity(entities.get(i));
		}
		world.renderWorld(renderer);
		renderer.render(lights, camera, clipPlane);

	}
	
	public void gameStart() {
		entities.clear();
		
		world.generateWorld();
		entities.add(new Player(new Vector3f(5000, 0, 5000), this));
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		if(entity instanceof Player) {
			player = (Player) entity;
		}
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public Player getPlayer() {
		return player;
	}

	public World getWorld() {
		return world;
	}

	public void cleanUp() {
		fbos.cleanUp();
		guiRenderer.cleanUp();
		waterRenderer.cleanUp();
		renderer.cleanUp();
	}

	public Camera getCamera() {
		return camera;
	}

}
