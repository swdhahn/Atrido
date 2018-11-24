package com.countgandi.com.engine.renderEngine.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.countgandi.com.Assets;
import com.countgandi.com.engine.renderEngine.Loader;
import com.countgandi.com.engine.renderEngine.font.fontMeshCreator.FontType;
import com.countgandi.com.engine.renderEngine.font.fontMeshCreator.GUIText;
import com.countgandi.com.engine.renderEngine.font.fontMeshCreator.TextMeshData;
import com.countgandi.com.engine.renderEngine.font.fontRendering.FontRenderer;

public class TextMaster {
	
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init() {
		renderer = new FontRenderer();
		TextMaster.loader = Assets.loader;
	}
	
	public static void render() {
		renderer.render(texts);
	}
	
	public static void load(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
			text.remove();
		}
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
}
