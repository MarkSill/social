package com.marksill.social.instance;

import org.dyn4j.dynamics.World;

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
