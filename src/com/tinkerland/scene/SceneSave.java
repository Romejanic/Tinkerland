package com.tinkerland.scene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tinkerland.lighting.Light;

public class SceneSave {

	public static final Gson GSON = new GsonBuilder().serializeNulls().create();
	public static final JsonParser PARSER = new JsonParser();
	
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

	public static Scene loadScene(File file) {
		JsonObject sceneJson = null;
		try {
			GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
			sceneJson = (JsonObject)PARSER.parse(new InputStreamReader(gzip));
		} catch(Exception e) {
			System.err.println("Failed to load scene file: " + file.getAbsolutePath());
			e.printStackTrace(System.err);
			return null;
		}
		
		Scene scene = new Scene();
		
		// load scene properties
		scene.sceneName = sceneJson.get("name").getAsString();
		scene.ambientColor = JsonHelper.deserializeColor(sceneJson.getAsJsonArray("ambientColor"));
		
		// load objects
		JsonArray objectsJson = sceneJson.getAsJsonArray("objects");
		for(int i = 0; i < objectsJson.size(); i++) {
			JsonObject objectJson = objectsJson.get(i).getAsJsonObject();
			SceneObject object    = SceneObject.deserializeObject(objectJson);
			if(object != null) {
				scene.addObject(object);
			}
		}
		
		// load lights
		JsonArray lightsJson = sceneJson.getAsJsonArray("lights");
		for(int i = 0; i < lightsJson.size(); i++) {
			JsonObject lightJson = lightsJson.get(i).getAsJsonObject();
			Light light          = Light.deserializeLight(lightJson);
			if(light != null) {
				scene.addLight(light);
			}
		}
		
		return scene;
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