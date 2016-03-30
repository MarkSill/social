package com.marksill.social.instance;

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
		if (!lastSize.equals(size)) {
			getBody().removeAllFixtures();
			Rectangle rect = new Rectangle(size.x, size.y);
			addShape(rect);
			lastSize = size.copy();
		}
	}

}
