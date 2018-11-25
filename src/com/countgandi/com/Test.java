package com.countgandi.com;

import java.io.IOException;

import com.countgandi.com.engine.audio.AudioMaster;
import com.countgandi.com.engine.audio.Source;

public class Test {

	public static void main(String[] args) throws IOException {
		AudioMaster.init();
		AudioMaster.setListenerData();
		int buffer = AudioMaster.loadSound("bounce");
		Source source = new Source();
		
		//while(true) {
		source.play(buffer);
		//}
		
		source.delete();
		AudioMaster.cleanUp();
	}

}
