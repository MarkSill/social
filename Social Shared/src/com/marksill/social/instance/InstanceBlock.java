package com.marksill.social.instance;

import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

public class InstanceBlock extends Instance implements Cloneable {
	
	public static final String CLASS_NAME = "Block";
	
	public boolean anchored = false;
	public Vector2 position = new Vector2();
	public Color color = Color.white;
	public boolean visible = true;
	public double mass = 1;
	public double density = 1;
	public double elasticity = 0.2;
	
	private Body body;
	private Vector2 lastPosition = new Vector2();
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
		if (getParent() instanceof InstanceWorld) {
			((InstanceWorld) Instance.game.findChild("World")).getWorld().addBody(body);
		}
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
	}
	
	public Body getBody() {
		return body;
	}
	
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
			((InstanceWorld) Instance.game.findChild("World")).getWorld().addBody(nBody);
		}
		return block;
	}
	
	@Override
	public void setParent(Instance parent) {
		if (parent instanceof InstanceWorld && !(getParent() instanceof InstanceWorld) && body != null) {
			((InstanceWorld) parent).getWorld().addBody(body);
		} else if (getParent() instanceof InstanceWorld) {
			((InstanceWorld) getParent()).getWorld().removeBody(body);
		}
		super.setParent(parent);
	}

}
