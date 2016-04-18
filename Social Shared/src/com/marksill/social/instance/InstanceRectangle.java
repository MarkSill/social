package com.marksill.social.instance;

import java.util.Map;

import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class InstanceRectangle extends InstanceBlock {
	
	public static String CLASS_NAME = "Rectangle";
	
	public Vector2 size;
	private Vector2 lastSize;

	public InstanceRectangle() {
		super(CLASS_NAME);
	}
	
	public InstanceRectangle(String name) {
		super(name);
	}
	
	public InstanceRectangle(Instance parent) {
		super(CLASS_NAME, parent);
	}
	
	public InstanceRectangle(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		super.init();
		size = new Vector2(1, 1);
		lastSize = size.copy();
		Rectangle rect = new Rectangle(size.x, size.y);
		addShape(rect);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (lastSize != null) {
			if (!lastSize.equals(size)) {
				getBody().removeAllFixtures();
				if (size.x == 0 && size.y == 0) {
					size = new Vector2(0.1, 0.1);
				}
				Rectangle rect = new Rectangle(size.x, size.y);
				addShape(rect);
				lastSize = size.copy();
			}
		} else {
			lastSize = new Vector2(1, 1);
		}
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("size", size);
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		size = (Vector2) map.get("size");
		lastSize = size.copy();
		getBody().removeAllFixtures();
		addShape(new Rectangle(size.x, size.y));
	}

}
