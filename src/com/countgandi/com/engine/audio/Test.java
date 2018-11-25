package com.countgandi.com.engine.audio;

import java.io.IOException;

import org.lwjgl.openal.AL10;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);

		int buffer = AudioMaster.loadSound("treeLeaves");
		Source source = new Source();
		source.setLooping(true);
		source.play(buffer);
		source.setPosition(0, 0, 0);
		float xPos = 0;
		

		char c = ' ';
		while (c != 'q') {

			xPos -= 0.01f;
			source.setPosition(xPos, 0, 0);
			System.out.println("pos: " + xPos);
			
			Thread.sleep(10);
		}

		source.delete();
		AudioMaster.cleanUp();
	}

}
