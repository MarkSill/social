package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

/**
 * A container class for physical objects.
 */
public class InstanceWorld extends Instance {
	
	/** The world's class name. */
	public static final String CLASS_NAME = "World";
	
	public double gravX;
	public double gravY;
	
	/** The world's World. */
	private World world;
	/** A list of bodies to add next update. */
	private List<Body> bodiesToAdd;
	private List<Body> bodiesToRemove;
	private double lastGravX;
	private double lastGravY;
	
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
		gravX = world.getGravity().x;
		gravY = world.getGravity().y;
		lastGravX = gravX;
		lastGravY = gravY;
		bodiesToAdd = new ArrayList<Body>();
		bodiesToRemove = new ArrayList<Body>();
		InstanceRectangle block = new InstanceRectangle(this);
		block.anchored = true;
		block.size = new Vector2(12, 1);
		block.position = new Vector2(10, 3);
		block.color = new Color(1.0f, 0.0f, 0.0f);
		block.elasticity = 1;
		InstanceRectangle block2 = new InstanceRectangle(this);
		block2.anchored = true;
		block2.size = new Vector2(1, 10);
		block2.position = new Vector2(2.5, 8.5);
		block2.color = new Color(1.0f, 0.0f, 0.0f);
		block2.elasticity = 1;
		new InstanceScript(this);
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
		copy = bodiesToRemove.toArray();
		bodiesToRemove.clear();
		for (int i = 0; i < copy.length; i++) {
			world.removeBody((Body) copy[i]);
		}
		if (gravX != lastGravX || gravY != lastGravY) {
			world.setGravity(new Vector2(gravX, gravY));
			lastGravX = gravX;
			lastGravY = gravY;
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
	
	public void removeBody(Body body) {
		bodiesToRemove.add(body);
	}

}
