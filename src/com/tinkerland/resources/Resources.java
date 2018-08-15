package com.tinkerland.resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Resources {

	public static String getResourcePath(String resource) {
		return "/res/" + (resource.startsWith("/") ? "" : "/") + resource;
	}

	public static InputStream getResource(String resource) throws FileNotFoundException {
		String path   = getResourcePath(resource);
		InputStream s = Resources.class.getResourceAsStream(path);
		if(s == null) {
			throw new FileNotFoundException(path);
		}
		return s;
	}
	
	public static String readFull(String resource) throws Exception {
		InputStream    stream = getResource(resource);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder  out    = new StringBuilder();
		for(String ln = reader.readLine(); ln != null; ln = reader.readLine()) {
			out.append(ln).append("\n");
		}
		reader.close();
		return out.toString().trim();
	}
	
	public static ArrayList<String> readLines(String resource) throws Exception {
		InputStream    stream = getResource(resource);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		ArrayList<String> out = new ArrayList<String>();
		for(String ln = reader.readLine(); ln != null; ln = reader.readLine()) {
			out.add(ln);
		}
		reader.close();
		return out;
	}
	
}