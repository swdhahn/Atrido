package com.countgandi.com.engine.renderEngine.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.countgandi.com.engine.Maths;
import com.countgandi.com.game.entities.Camera;
import com.countgandi.com.game.entities.Light;

public class StaticShader extends ShaderProgram {
	
	private static final int MaxLights = 4;
	
	private static final String VERTEX_FILE = "src/com/countgandi/com/engine/renderEngine/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/com/countgandi/com/engine/renderEngine/shaders/fragmentShader.txt";
	
	public UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	public UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	public UniformFloat lightPosition[] = new UniformFloat[MaxLights];
	public UniformFloat lightColor[] = new UniformFloat[MaxLights];
	public UniformFloat attenuation[] = new UniformFloat[MaxLights];
	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	public UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	public UniformVec3 skyColor = new UniformVec3("skyColor");
	public UniformInt numberOfRows = new UniformInt("numberOfRows");
	public UniformVec2 offset = new UniformVec2("offset");
	public UniformVec4 plane = new UniformVec4("plane");
	
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		
		for(int i = 0; i < MaxLights; i++) {
			lightPosition
			
			
		}
		
		super.getAllUniformLocations(uniforms);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	public void loadLights (List<Light> lights) {
		for(int i = 0 ; i < MaxLights; i++) {
			if(i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
}
