package com.marksill.social.instance;

import java.util.Map;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

import com.marksill.social.Social;
import com.marksill.social.state.NotGameState;

public class InstanceCamera extends Instance {
	
	public static final String CLASS_NAME = "Camera";
	
	public Vector2 position;
	public Vector2 scale;
	public Vector2 size;

	public InstanceCamera() {
		super(CLASS_NAME);
	}

	public InstanceCamera(String name) {
		super(name);
	}

	public InstanceCamera(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceCamera(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		position = new Vector2();
		scale = new Vector2(0.5, 0.5);
		size = new Vector2(800, 600);
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("position", position);
		map.put("scale", scale);
		map.put("size", size);
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		if (map.get("position") != null) {
			position = (Vector2) map.get("position");
		}
		if (map.get("scale") != null) {
			scale = (Vector2) map.get("scale");
		}
		if (map.get("size") != null) {
			size = (Vector2) map.get("size");
		}
	}
	
	public void translate(Graphics g) {
		g.translate((float) -position.x * NotGameState.PPM + ((float) (Social.getInstance().getContainer().getWidth() / scale.x) / 2), (float) -position.y * NotGameState.PPM - ((float) (Social.getInstance().getContainer().getHeight() / scale.y) / 2 - (float) (Social.getInstance().getContainer().getHeight() * (1.5 - scale.y))));
	}
	
	public void zoom(Graphics g) {
		g.scale((float) scale.x, (float) scale.y);
	}
	
	public static InstanceCamera getCamera() {
		InstanceCamera cam = null;
		Social social = Social.getInstance();
		if ((social.isNetworked() && social.isServer()) || (social.isNetworked() && InstancePlayer.pid == -1)) {
			cam = InstancePlayer.camera;
		} else {
			cam = (InstanceCamera) ((InstancePlayer) Instance.getByID(InstancePlayer.pid)).findChild("Camera");
		}
		return cam;
	}

}
