package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

/**
 * A container class for physical objects.
 */
public class InstanceWorld extends Instance {
	
	/** The world's class name. */
	public static final String CLASS_NAME = "World";
	
	/** The world's World. */
	private World world;
	/** A list of bodies to add next update. */
	private List<Body> bodiesToAdd;
	
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
		bodiesToAdd = new ArrayList<Body>();
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		world.update((double) delta / 1000);
		Object[] copy = bodiesToAdd.toArray();
		bodiesToAdd.clear();
		for (int i = 0; i < copy.length; i++) {
			world.addBody((Body) copy[i]);
		}
	}
	
	/**
	 * Gets the world's World.
	 * @return The world's World.
	 */
	public World getWorld() {
		return world;
	}
	
	public void addBody(Body body) {
		bodiesToAdd.add(body);
	}

}
