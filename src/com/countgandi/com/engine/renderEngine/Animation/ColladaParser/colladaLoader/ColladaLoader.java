package com.countgandi.com.engine.renderEngine.Animation.ColladaParser.colladaLoader;

import java.io.File;

import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.dataStructures.AnimatedModelData;
import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.dataStructures.AnimationData;
import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.dataStructures.MeshData;
import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.dataStructures.SkeletonData;
import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.dataStructures.SkinningData;
import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.xmlParser.XmlNode;
import com.countgandi.com.engine.renderEngine.Animation.ColladaParser.xmlParser.XmlParser;

public class ColladaLoader {

	public static AnimatedModelData loadColladaModel(File colladaFile, int maxWeights) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);

		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();

		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		SkeletonData jointsData = jointsLoader.extractBoneData();

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();

		return new AnimatedModelData(meshData, jointsData);
	}

	public static AnimationData loadColladaAnimation(File colladaFile) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}

}
