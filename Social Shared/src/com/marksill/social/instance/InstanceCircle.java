package com.marksill.social.instance;

import java.util.Map;

import org.dyn4j.geometry.Circle;

public class InstanceCircle extends InstanceBlock {
	
	public static String CLASS_NAME = "Circle";
	
	public double radius;
	private double lastRadius;
	
	public InstanceCircle() {
		super(CLASS_NAME);
	}
	
	public InstanceCircle(String name) {
		super(name);
	}
	
	public InstanceCircle(Instance parent) {
		super(CLASS_NAME, parent);
	}
	
	public InstanceCircle(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		super.init();
		radius = 1;
		lastRadius = radius;
		Circle circ = new Circle(radius);
		addShape(circ);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (lastRadius != radius) {
			getBody().removeAllFixtures();
			Circle circ = new Circle(radius);
			addShape(circ);
			lastRadius = radius;
		}
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("radius", radius);
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		if (map.get("radius") != null) {
			radius = (double) map.get("radius");
		}
	}

}
