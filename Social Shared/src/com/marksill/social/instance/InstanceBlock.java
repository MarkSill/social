package com.marksill.social.instance;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

public class InstanceBlock extends Instance {
	
	public static final String CLASS_NAME = "Block";
	
	public boolean anchored = false;
	public Vector2 position = new Vector2();
	public Vector2 size = new Vector2();
	public Color color = Color.white;
	public boolean visible = true;
	public double mass = 1;
	public double density = 1;
	public double elasticity = 0.2;
	
	private Body body;
	private Vector2 lastPosition = new Vector2();
	private Vector2 lastSize = new Vector2();
	private double lastMass = 1;
	private double lastDensity = 1;
	private double lastElasticity = 0.2;

	public InstanceBlock() {
		super(CLASS_NAME);
	}

	public InstanceBlock(String name) {
		super(name);
	}

	public InstanceBlock(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceBlock(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		body = new Body();
		Rectangle shape = new Rectangle(1, 1);
		shape.createMass(mass);
		BodyFixture fixture = new BodyFixture(shape);
		fixture.setDensity(density);
		fixture.setRestitution(elasticity);
		fixture.createMass();
		body.addFixture(fixture);
		body.setMass(MassType.NORMAL);
		((InstanceWorld) Instance.game.findChild("World")).getWorld().addBody(body);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (anchored) {
			body.setMass(MassType.INFINITE);
		} else {
			body.setMass(MassType.NORMAL);
		}
		if (!position.difference(lastPosition).isZero()) {
			body.translate(position.difference(body.getWorldCenter()));
		}
		lastPosition = position;
		if (!size.difference(lastSize).isZero()) {
			body.removeAllFixtures();
			Rectangle shape = new Rectangle(size.x, size.y);
			shape.createMass(mass);
			BodyFixture fixture = new BodyFixture(shape);
			fixture.setDensity(density);
			fixture.setRestitution(elasticity);
			fixture.createMass();
			body.addFixture(fixture);
		}
		lastSize = size;
		if (mass != lastMass) {
			BodyFixture fixture = body.getFixture(0);
			fixture.getShape().createMass(mass);
			fixture.createMass();
		}
		lastMass = mass;
		if (density != lastDensity) {
			BodyFixture fixture = body.getFixture(0);
			fixture.setDensity(density);
			fixture.createMass();
		}
		lastDensity = density;
		if (elasticity != lastElasticity) {
			BodyFixture fixture = body.getFixture(0);
			fixture.setRestitution(elasticity);
		}
		lastElasticity = elasticity;
	}
	
	public Body getBody() {
		return body;
	}

}
