package com.marksill.social.instance;

import java.util.List;
import java.util.Map;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

/**
 * Base class for physical objects. Contrary to its name, this class does not only have to be blocks.
 */
public class InstanceBlock extends Instance implements Cloneable {
	
	/** The block's class name. */
	public static final String CLASS_NAME = "Block";
	
	/** The dynamic status of the block. */
	public boolean anchored = false;
	/** The position of the block. */
	public Vector2 position = new Vector2();
	/** The color of the block */
	public Color color = Color.white;
	/** The visibility status of the block. */
	public boolean visible = true;
	/** The mass of the block. */
	public double mass = 1;
	/** The density of the block. */
	public double density = BodyFixture.DEFAULT_DENSITY;
	/** The elasticity of the block. */
	public double elasticity = BodyFixture.DEFAULT_RESTITUTION;
	public double friction = BodyFixture.DEFAULT_FRICTION;
	public boolean rotationLocked = false;
	
	/** The physical body of the block. */
	private Body body;
	/** The block's position last update. */
	private Vector2 lastPosition = new Vector2();
	/** The block's mass last update. */
	private double lastMass = 1;
	/** The block's density last update. */
	private double lastDensity = 1;
	/** The block's elasticity last update. */
	private double lastElasticity = 0.2;
	private double lastFriction = 0.2;

	/**
	 * Creates a new block.
	 */
	public InstanceBlock() {
		super(CLASS_NAME);
	}

	/**
	 * Creates a new block.
	 * @param name The name of the block.
	 */
	public InstanceBlock(String name) {
		super(name);
	}

	/**
	 * Creates a new block.
	 * @param parent The parent of the block.
	 */
	public InstanceBlock(Instance parent) {
		super(CLASS_NAME, parent);
	}

	/**
	 * Creates a new block.
	 * @param name The name of the block.
	 * @param parent The parent of the block.
	 */
	public InstanceBlock(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		anchored = false;
		if (position == null) {
			position = new Vector2();
		}
		color = Color.white;
		visible = true;
		mass = 1;
		density = BodyFixture.DEFAULT_DENSITY;
		elasticity = BodyFixture.DEFAULT_RESTITUTION;
		friction = BodyFixture.DEFAULT_FRICTION;
		lastPosition = new Vector2();
		lastMass = mass;
		lastDensity = density;
		lastElasticity = elasticity;
		lastFriction = friction;
		body = new Body();
		setParent(getParent());
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (body != null) {
			if (anchored) {
				body.setMass(MassType.INFINITE);
				body.setLinearVelocity(0, 0);
				body.setAngularVelocity(0);
			} else {
				body.setMass(MassType.NORMAL);
			}
			if (!position.difference(lastPosition).isZero()) {
				body.translate(position.difference(body.getWorldCenter()));
			}
			position = body.getWorldCenter();
			lastPosition = position;
			if (mass != lastMass) {
				for (BodyFixture f : body.getFixtures()) {
					f.getShape().createMass(mass);
					f.createMass();
				}
			}
			lastMass = mass;
			if (density != lastDensity) {
				for (BodyFixture f : body.getFixtures()) {
					f.setDensity(density);
					f.createMass();
				}
			}
			lastDensity = density;
			if (elasticity != lastElasticity) {
				for (BodyFixture f : body.getFixtures()) {
					f.setRestitution(elasticity);
				}
			}
			lastElasticity = elasticity;
			if (friction != lastFriction) {
				for (BodyFixture f : body.getFixtures()) {
					f.setFriction(friction);
				}
			}
			lastFriction = friction;
			if (rotationLocked) {
				body.getTransform().setRotation(0);
			}
		}
	}
	
	/**
	 * Gets the physical body of the block.
	 * @return The body of the block.
	 */
	public Body getBody() {
		return body;
	}
	
	/**
	 * Adds a shape to the block.
	 * @param shape The shape to add to the block.
	 */
	public void addShape(Convex shape) {
		shape.createMass(mass);
		BodyFixture fixture = new BodyFixture(shape);
		fixture.setDensity(density);
		fixture.setRestitution(elasticity);
		fixture.setFriction(friction);
		fixture.createMass();
		body.addFixture(fixture);
	}
	
	@Override
	public InstanceBlock clone() {
		InstanceBlock block = (InstanceBlock) super.clone();
		Body body = block.getBody();
		Body nBody = new Body();
		nBody.setTransform(body.getTransform());
		List<BodyFixture> fixtures = body.getFixtures();
		block.body = nBody;
		for (BodyFixture f : fixtures) {
			Convex shape = f.getShape();
			block.addShape(shape);
		}
		if (getParent() instanceof InstanceWorld) {
			((InstanceWorld) Instance.game.findChild("World")).addBody(nBody);
		}
		return block;
	}
	
	@Override
	public void setParent(Instance parent) {
		InstanceWorld world = (InstanceWorld) Instance.game.findChild("World");
		if (parent != null && (parent instanceof InstanceWorld || parent.childOf(world)) && body != null) {
			world.addBody(body);
		} else if (getParent() != null && (getParent() instanceof InstanceWorld || getParent().childOf(world)) && body != null) {
			world.removeBody(body);
		}
		super.setParent(parent);
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("anchored", anchored);
		map.put("position", position);
		map.put("color", color);
		map.put("visible", visible);
		map.put("mass", mass);
		map.put("density", density);
		map.put("elasticity", elasticity);
		map.put("friction", friction);
		map.put("rotationLocked", rotationLocked);
		map.put("transform", body.getTransform());
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		if (map.get("anchored") != null) {
			anchored = (boolean) map.get("anchored");
		}
		if (map.get("position") != null) {
			position = (Vector2) map.get("position");
		}
		if (map.get("color") != null) {
			color = (Color) map.get("color");
		}
		if (map.get("visible") != null) {
			visible = (boolean) map.get("visible");
		}
		if (map.get("mass") != null) {
			mass = (double) map.get("mass");
		}
		if (map.get("density") != null) {
			density = (double) map.get("density");
		}
		if (map.get("elasticity") != null) {
			elasticity = (double) map.get("elasticity");
		}
		if (map.get("friction") != null) {
			friction = (double) map.get("friction");
		}
		if (map.get("rotationLocked") != null) {
			rotationLocked = (boolean) map.get("rotationLocked");
		}
		lastPosition = position.copy();
		body.translate(position);
		body.setTransform((Transform) map.get("transform"));
	}

}
