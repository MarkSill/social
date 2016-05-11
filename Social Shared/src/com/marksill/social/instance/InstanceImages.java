package com.marksill.social.instance;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.PNGImageData;

import com.marksill.social.Social;

public class InstanceImages extends Instance {
	
	public static final String CLASS_NAME = "Images";
	
	private static Map<String, Image> images = new HashMap<>();
	private static Map<String, String> toLoad = new HashMap<>();
	private static Map<String, String> src = new HashMap<>();

	public InstanceImages() {
		super(CLASS_NAME);
	}

	public InstanceImages(String name) {
		super(name);
	}

	public InstanceImages(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceImages(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void updateVars() {
		for (String key : toLoad.keySet()) {
			if (images.get(key) == null) {
				images.put(key, loadImage(toLoad.get(key)));
				src.put(key, toLoad.get(key));
			}
		}
		toLoad.clear();
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("imgs", src);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		toLoad = (Map<String, String>) map.get("imgs");
	}
	
	public static void registerImage(String name, String file) {
		toLoad.put(name, file);
	}
	
	public static Image getImage(String name) {
		return images.get(name);
	}
	
	public static Image loadImage(String src) {
		Image img = null;
		try {
			URL url = new URL("https://social.marksill.com/assets/" + src + ".png");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(Social.getInstance().getSSLSocketFactory());
			InputStream stream = connection.getInputStream();
			PNGImageData data = new PNGImageData();
			data.loadImage(stream, false, true, null);
			img = new Image(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
