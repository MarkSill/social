package com.marksill.social.instance;

import org.dyn4j.dynamics.World;
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
		dyn.position = new Vector2(10, 4);
		dyn.size = new Vector2(1, 2);
		InstanceBlock anchored = new InstanceBlock("Anchored", this);
		anchored.anchored = true;
		anchored.color = Color.red;
		anchored.size = new Vector2(20, 1);
		anchored.position = new Vector2(anchored.size.x / 2 + 1, 1);
		anchored.elasticity = 1.01;
		InstanceBlock top = (InstanceBlock) anchored.clone();
		top.position = new Vector2(top.size.x / 2 + 1, 20);
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
