package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Vector2;

/**
 * A container class for physical objects.
 */
public class InstanceWorld extends Instance implements CollisionListener {
	
	/** The world's class name. */
	public static final String CLASS_NAME = "World";
	
	public double gravX;
	public double gravY;
	public boolean physicsEnabled;
	public double speed;
	
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
		world.setGravity(new Vector2(0, -9.81));
		gravX = world.getGravity().x;
		gravY = world.getGravity().y;
		speed = 1;
		lastGravX = gravX;
		lastGravY = gravY;
		physicsEnabled = true;
		bodiesToAdd = new ArrayList<Body>();
		bodiesToRemove = new ArrayList<Body>();
		world.addListener(this);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (physicsEnabled) {
			Object[] copy = bodiesToAdd.toArray();
			bodiesToAdd.clear();
			for (int i = 0; i < copy.length; i++) {
				if (copy[i] != null) {
					if (!world.containsBody((Body) copy[i])) {
						world.addBody((Body) copy[i]);
					}
				}
			}
			copy = bodiesToRemove.toArray();
			bodiesToRemove.clear();
			for (int i = 0; i < copy.length; i++) {
				world.removeBody((Body) copy[i]);
			}
			world.updatev(((double) delta / 1000.0) * speed);
		}
	}
	
	@Override
	public void updateVars() {
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
	
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("gravX", gravX);
		map.put("gravY", gravY);
		return map;
	}
	
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		if (map.get("gravX") != null) {
			gravX = (double) map.get("gravX");
		}
		if (map.get("gravY") != null) {
			gravY = (double) map.get("gravY");
		}
	}

	@Override
	public boolean collision(ContactConstraint arg0) {
		return true;
	}

	@Override
	public boolean collision(Body b1, BodyFixture bf1, Body b2, BodyFixture bf2) {
		InstanceBlock block1 = InstanceBlock.getBlockByBody(b1);
		InstanceBlock block2 = InstanceBlock.getBlockByBody(b2);
		return block1.collision(block2);
	}

	@Override
	public boolean collision(Body arg0, BodyFixture arg1, Body arg2, BodyFixture arg3, Penetration arg4) {
		return true;
	}

	@Override
	public boolean collision(Body arg0, BodyFixture arg1, Body arg2, BodyFixture arg3, Manifold arg4) {
		return true;
	}

}
