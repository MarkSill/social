package com.marksill.social.instance;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

public class InstanceWorld extends Instance {
	
	public static final String CLASS_NAME = "World";
	
	private World world;

	public InstanceWorld() {
		super(CLASS_NAME);
	}

	public InstanceWorld(String name) {
		super(name);
	}

	public InstanceWorld(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceWorld(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		world = new World();
		world.setGravity(World.EARTH_GRAVITY);
		InstanceBlock dyn = new InstanceBlock(this);
		dyn.position = new Vector2(4, 4);
		dyn.addShape(new Circle(0.5));
		Rectangle rect = new Rectangle(0.5, 1);
		rect.translate(0.25, 0);
		dyn.addShape(rect);
		dyn.getBody().rotate(Math.toRadians(91));
		InstanceBlock bottom = new InstanceBlock("Bottom", this);
		bottom.anchored = true;
		bottom.color = Color.red;
		bottom.addShape(new Rectangle(10, 1));
		bottom.position = new Vector2(5.5, 2);
		bottom.elasticity = 0.2;
		InstanceBlock top = bottom.clone();
		top.color = Color.blue;
		top.position = new Vector2(5.5, 6);
		top.setParent(this);
		InstanceBlock left = top.clone();
		left.color = Color.green;
		left.getBody().removeAllFixtures();
		left.addShape(new Rectangle(1, 5));
		left.position = new Vector2(0.5, 4);
		left.setParent(this);
		InstanceBlock right = left.clone();
		right.color = Color.orange;
		right.position = new Vector2(11, 4);
		right.setParent(this);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		world.update((double) delta / 1000);
	}
	
	public World getWorld() {
		return world;
	}

}
