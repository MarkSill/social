package com.marksill.social.instance;

import org.dyn4j.dynamics.World;

/**
 * A container class for physical objects.
 */
public class InstanceWorld extends Instance {
	
	/** The world's class name. */
	public static final String CLASS_NAME = "World";
	
	/** The world's World. */
	private World world;
	
	//Temporary testing variables:
	int i = 0;
	InstanceScript script;

	/**
	 * Creates a new world.
	 */
	public InstanceWorld() {
		super(CLASS_NAME);
	}

	/**
	 * Creates a new world.
	 * @param name The name of the world.
	 */
	public InstanceWorld(String name) {
		super(name);
	}

	/**
	 * Creates a new world.
	 * @param parent The world's parent.
	 */
	public InstanceWorld(Instance parent) {
		super(CLASS_NAME, parent);
	}

	/**
	 * Creates a new world.
	 * @param name The name of the world.
	 * @param parent The world's parent.
	 */
	public InstanceWorld(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		world = new World();
		world.setGravity(World.EARTH_GRAVITY);
		script = new InstanceScript(this);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		world.update((double) delta / 1000);
		if (i++ > 100) {
			script.enabled = false;
		}
	}
	
	/**
	 * Gets the world's World.
	 * @return The world's World.
	 */
	public World getWorld() {
		return world;
	}

}
