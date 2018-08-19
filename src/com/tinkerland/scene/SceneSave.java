package com.tinkerland.scene;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tinkerland.lighting.Light;

public class SceneSave {

	public static final Gson GSON = new GsonBuilder().serializeNulls().create();

	public static boolean saveScene(Scene scene, File file) {
		JsonObject sceneJson = new JsonObject();

		// serialize scene properties
		sceneJson.addProperty("name", scene.sceneName);
		sceneJson.add("ambientColor", JsonHelper.serializeColor(scene.ambientColor));

		// serialize scene objects
		JsonArray objects = new JsonArray();
		for(SceneObject object : scene.getSceneObjects()) {
			JsonObject objectJson = new JsonObject();
			object.serializeObject(objectJson);
			objects.add(objectJson);
		}
		sceneJson.add("objects", objects);

		// serialize lights
		JsonArray lights = new JsonArray();
		for(Light light : scene.getLights()) {
			JsonObject lightJson = new JsonObject();
			light.serializeLight(lightJson);
			lights.add(lightJson);
		}
		sceneJson.add("lights", lights);

		// save json to file
		try {
			String json = GSON.toJson(sceneJson);
			writeZippedStringToFile(json, file);
		} catch(Exception e) {
			System.err.println("Failed to save scene to file!");
			System.err.println("File: " + file.getAbsolutePath());
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}

	private static void writeZippedStringToFile(String data, File file) throws Exception {
		if(!file.exists() && !file.createNewFile()) {
			throw new IOException("Failed to create " + file.getAbsolutePath());
		}
		FileOutputStream fout = new FileOutputStream(file);
		GZIPOutputStream gzip = new GZIPOutputStream(fout);
		PrintWriter writer = new PrintWriter(gzip);
		writer.print(data);
		writer.flush();
		writer.close();
	}

}