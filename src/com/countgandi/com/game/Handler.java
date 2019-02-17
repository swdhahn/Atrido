package com.countgandi.com.game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.countgandi.com.creationEngine.IndependentCamera;
import com.countgandi.com.engine.renderEngine.MasterRenderer;
import com.countgandi.com.engine.renderEngine.font.TextMaster;
import com.countgandi.com.engine.renderEngine.guis.GuiRenderer;
import com.countgandi.com.engine.renderEngine.guis.GuiTexture;
import com.countgandi.com.engine.renderEngine.water.WaterFrameBuffers;
import com.countgandi.com.engine.renderEngine.water.WaterRenderer;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Entity;
import com.countgandi.com.game.entities.Light;
import com.countgandi.com.game.entities.Player;
import com.countgandi.com.game.entities.structures.StoneFlooring;
import com.countgandi.com.game.guis.InventoryGui;
import com.countgandi.com.game.worldGen.World;
import com.countgandi.com.net.ProxySide;
import com.countgandi.com.net.ProxySide.ProxyType;

public class Handler {

	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<GuiTexture> guis = new ArrayList<GuiTexture>();
	public ArrayList<Light> lights = new ArrayList<Light>();
	public ArrayList<WaterTile> waters = new ArrayList<WaterTile>();

	private WaterRenderer waterRenderer;
	private WaterFrameBuffers fbos;
	public MasterRenderer renderer;
	private GuiRenderer guiRenderer;
	private Camera camera;

	private InventoryGui inventoryGui;

	private Player player;
	private World world;
	private boolean isEngine;

	public Handler(boolean isEngine) {
		if (isEngine) {
			this.camera = new IndependentCamera(this);
		} else {
			this.camera = new Camera(this);
		}
		renderer = new MasterRenderer(camera, Assets.loader);
		guiRenderer = new GuiRenderer(Assets.loader);
		this.isEngine = isEngine;

		TextMaster.init(Assets.loader);
		fbos = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(camera, fbos, Assets.loader);
		world = new World(this);
		inventoryGui = new InventoryGui(this);
	}

	private static int sunRADIUS = 1000;
	private double sunangle = 0;

	public void tick() {
		camera.move();
		world.updateTerrain();

		double x = sunRADIUS * Math.cos(sunangle);
		double y = (sunangle / (float) sunRADIUS);
		double z = sunRADIUS * Math.sin(sunangle);
		//lights.get(0).move(new Vector3f((float) x, (float) y, (float) z));
		sunangle += 0.005f;

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			Light light = new Light(new Vector3f(camera.getPosition().x, camera.getPosition().y + 5, camera.getPosition().z), new Vector3f(0, 0.5f, 1), Light.LIGHT_LAMP);
			light.setAttenuation(new Vector3f(0.5f, 0.2f, 0.05f));
			lights.add(light);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			entities.add(new StoneFlooring(new Vector3f(camera.getPosition().x, camera.getPosition().y - 5, camera.getPosition().z), this) {
			});
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			entities.add(new Entity(Assets.shipModel, new Vector3f(camera.getPosition().x, camera.getPosition().y - 5, camera.getPosition().z), new Vector3f(0, 0, 0), 10, this) {
			});
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_E) {
			if (!Keyboard.getEventKeyState()) {
				if (inventoryGui.isOpen()) {
					inventoryGui.remove();
				} else {
					inventoryGui.add();
				}
			}
		}
		
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
		renderScene(new Vector4f(0, 1, 0, -waters.get(0).getHeight()));
		camera.getPosition().y += distance;
		camera.invertPitch();

		fbos.bindRefractionFrameBuffer();
		renderScene(new Vector4f(0, -1, 0, waters.get(0).getHeight() + 1));

		fbos.unbindCurrentFrameBuffer();

		renderScene(new Vector4f(0, -1, 0, 10000));
		waterRenderer.render(waters, camera, lights.get(0));

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

	public void changeToIndependantCamera() {
		this.camera = new IndependentCamera(this);
	}

}
