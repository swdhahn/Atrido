package com.countgandi.com.game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.countgandi.com.creationEngine.IndependentCamera;
import com.countgandi.com.engine.guis.GuiRenderer;
import com.countgandi.com.engine.guis.GuiTexture;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.font.TextMaster;
import com.countgandi.com.engine.renderEngine.particles.ParticleMaster;
import com.countgandi.com.engine.renderEngine.water.WaterFrameBuffers;
import com.countgandi.com.engine.renderEngine.water.WaterRenderer;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.game.entities.Player;
import com.countgandi.com.game.structures.Structure;
import com.countgandi.com.game.structures.StructureHut;
import com.countgandi.com.game.worldGen.World;

public class Handler {

	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Structure> structures = new ArrayList<Structure>();
	public ArrayList<GuiTexture> guis = new ArrayList<GuiTexture>();
	public ArrayList<Light> lights = new ArrayList<Light>();
	public ArrayList<WaterTile> waters = new ArrayList<WaterTile>();

	private WaterRenderer waterRenderer;
	private WaterFrameBuffers fbos;
	public MasterRenderer renderer;
	private GuiRenderer guiRenderer;
	private Camera camera;

	private Player player;
	private World world;
	private boolean isEngine;

	public Handler(boolean isEngine) {
		if(isEngine) {
			this.camera = new IndependentCamera(this);
		} else {
			this.camera = new Camera(this);
		}
		renderer = new MasterRenderer(camera, Assets.loader);
		guiRenderer = new GuiRenderer(Assets.loader);
		this.isEngine = isEngine;

		ParticleMaster.init(camera.getProjectionViewMatrix(), Assets.loader);
		TextMaster.init(Assets.loader);
		fbos = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(camera, fbos, Assets.loader);
		world = new World(this);
	}

	public void tick() {
		camera.move();
		lights.get(0).setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y + 200, camera.getPosition().z - 100));
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			structures.add(new StructureHut(new Vector3f(camera.getPosition().x, camera.getPosition().y - 2, camera.getPosition().z), new Vector3f(0, camera.getYaw(), 0), this));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			entities.add(new Entity(Assets.TexturedModels.ship, new Vector3f(camera.getPosition().x, camera.getPosition().y - 5, camera.getPosition().z), new Vector3f(0, camera.getYaw(), 0), 10f, this) {
			});
		}
		// new Particle(new Vector3f(camera.getPosition().x,
		// camera.getPosition().y, camera.getPosition().z - 10), new Vector3f(0,
		// 30, 0), 0.1F, 100, 0, 10);
		ParticleMaster.tick();
	}

	public void render(boolean isEngine) {
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

		if (!isEngine) {
			// 2D stuff
			guiRenderer.render(guis);
			TextMaster.render();
		}
	}

	private void renderScene(Vector4f clipPlane) {
		for (int i = 0; i < entities.size(); i++) {
			renderer.processEntity(entities.get(i));
		}
		for (int i = 0; i < structures.size(); i++) {
			renderer.processStructure(structures.get(i));
		}
		world.renderWorld(renderer);
		renderer.render(lights, camera, clipPlane);

	}

	public void gameStart() {
		entities.clear();

		world.generateWorld();
		entities.add(new Player(new Vector3f(0, 0, 0), this));
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
		if (entity instanceof Player) {
			player = (Player) entity;
		}
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public void addStructure(Structure structure) {
		structures.add(structure);
	}

	public void removeStructure(Structure structure) {
		structures.remove(structure);
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

	public boolean getIsEngine() {
		return isEngine;
	}

}
