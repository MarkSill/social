package com.marksill.social.instance;

import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
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
	public double density = 1;
	/** The elasticity of the block. */
	public double elasticity = 0.2;
	
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
		body = new Body();
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (body != null) {
			if (anchored) {
				body.setMass(MassType.INFINITE);
			} else {
				body.setMass(MassType.NORMAL);
			}
			if (!position.difference(lastPosition).isZero()) {
				body.translate(position.difference(body.getWorldCenter()));
			}
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
			if (getParent() instanceof InstanceWorld) {
				InstanceWorld world = (InstanceWorld) getParent();
				if (!world.getWorld().containsBody(body)) {
					world.addBody(body);
				}
			} else {
				InstanceWorld world = (InstanceWorld) Instance.game.findChild("World");
				if (world.getWorld().containsBody(body)) {
					world.removeBody(body);
				}
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
		fixture.createMass();
		body.addFixture(fixture);
	}
	
	@Override
	public InstanceBlock clone() {
		InstanceBlock block = (InstanceBlock) super.clone();
		block.body = new Body();
		block.body.setTransform(body.getTransform().copy());
		block.mass = mass;
		block.elasticity = elasticity;
		block.density = density;
		List<BodyFixture> fixtures = this.body.getFixtures();
		for (BodyFixture f : fixtures) {
			Convex shape = f.getShape();
			Convex newShape = null;
			if (shape instanceof Rectangle) {
				Rectangle rect = (Rectangle) shape;
				newShape = new Rectangle(rect.getWidth(), rect.getHeight());
			} else if (shape instanceof Circle) {
				Circle circ = (Circle) shape;
				newShape = new Circle(circ.getRadius());
			}
			block.addShape(newShape);
		}
		return block;
	}

}
