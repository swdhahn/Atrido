package com.countgandi.com.engine.renderEngine.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.game.entities.Camera;

public class ParticleMaster {
	
	private static List<Particle> particles = new ArrayList<Particle>();
	private static ParticleRenderer renderer;
	
	public static void init(Matrix4f projectionMatrix, Loader loader) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}
	
	public static void tick() {
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()) {
			Particle p = iterator.next();
			boolean stillAlive = p.tick();
			if(stillAlive) {
				iterator.remove();
			}
		}
	}
	
	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle p) {
		particles.add(p);
	}

}
