package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
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
	/** The friction of the block. */
	public double friction = BodyFixture.DEFAULT_FRICTION;
	/** Locks the rotation of the block. */
	public boolean rotationLocked = false;
	/** The rotation of the block. */
	public double rotation;
	/** The velocity of the block. */
	public Vector2 velocity;
	public String image;
	
	private Body body;
	private Vector2 lastPosition = new Vector2();
	private double lastMass = 1;
	private double lastDensity = 1;
	private double lastElasticity = 0.2;
	private double lastFriction = 0.2;
	private double lastRotation;
	private Vector2 lastVelocity;
	private List<Joint> joints;
	private List<LuaValue> collisionCallbacks;

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
		rotationLocked = false;
		rotation = 0;
		velocity = new Vector2();
		image = null;
		lastPosition = new Vector2();
		lastMass = mass;
		lastDensity = density;
		lastElasticity = elasticity;
		lastFriction = friction;
		lastRotation = 0;
		lastVelocity = velocity.copy();
		joints = new ArrayList<>();
		collisionCallbacks = new ArrayList<>();
		body = new Body();
		setParent(getParent());
	}
	
	@Override
	public void updateVars() {
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
				body.getTransform().setRotation(rotation);
			}
			if (rotation != lastRotation) {
				body.getTransform().setRotation(Math.toRadians(rotation));
				lastRotation = rotation;
			} else {
				rotation = body.getTransform().getRotation();
				lastRotation = rotation;
			}
			if (!velocity.difference(lastVelocity).isZero()) {
				body.setLinearVelocity(velocity);
			} else {
				velocity = body.getLinearVelocity();
			}
			lastVelocity = velocity.copy();
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
	
	public boolean collision(InstanceBlock other) {
		boolean val = true;
		for (LuaValue v : collisionCallbacks) {
			LuaValue result = v.call(CoerceJavaToLua.coerce(this), CoerceJavaToLua.coerce(other));
			if (val && (result == LuaValue.NIL || result == LuaValue.TRUE)) {
				val = true;
			} else if (result == LuaValue.FALSE) {
				val = false;
			}
		}
		return val;
	}
	
	public void addCollisionCallback(LuaValue func) {
		collisionCallbacks.add(func);
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
		int mode = 0; //0 = do nothing; 1 = add; 2 = remove
		if (childOf(world) && (parent == null || (!parent.childOf(world) && parent != world))) {
			mode = 2;
		} else if (parent != null && (parent.childOf(world) || parent == world)) {
			mode = 1;
		}
		if (mode == 1) {
			world.addBody(body);
		} else if (mode == 2) {
			world.removeBody(body);
		}
		super.setParent(parent);
	}
	
	public void addJoint(Joint joint) {
		joints.add(joint);
	}
	
	public List<Joint> getJointsAsList() {
		return new ArrayList<Joint>(joints);
	}
	
	public LuaTable getJoints() {
		List<Joint> joints = getJointsAsList();
		LuaValue[] values = new LuaValue[joints.size()];
		for (int i = 0; i < joints.size(); i++) {
			values[i] = CoerceJavaToLua.coerce(joints.get(i));
		}
		return LuaTable.listOf(values);
	}
	
	@Override
	public void delete() {
		InstanceWorld world = (InstanceWorld) Instance.game.findChild("World");
		if (world != null) {
			world.removeBody(body);
		}
		super.delete();
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
		if (body != null) {
			map.put("transform", body.getTransform());
			map.put("rotation", body.getTransform().getRotation());
		} else {
			map.put("transform", null);
		}
		map.put("image", image);
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		if (map.get("anchored") != null) {
			anchored = (boolean) map.get("anchored");
		}
		if (map.get("position") != null) {
			body.getTransform().translate(body.getTransform().getTranslation().negate());
			position = (Vector2) map.get("position");
			body.getTransform().translate(position);
			lastPosition = position.copy();
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
		if (map.get("transform") != null) {
			body.setTransform((Transform) map.get("transform"));
		}
		if (map.get("rotation") != null) {
			body.getTransform().setRotation((double) map.get("rotation"));
		}
		if (map.get("image") != null) {
			image = (String) map.get("image");
		}
	}
	
	public static InstanceBlock getBlockByBody(Body body) {
		for (Instance i : Instance.getInstancesAsList()) {
			if (i instanceof InstanceBlock) {
				InstanceBlock block = (InstanceBlock) i;
				if (block.body.equals(body)) {
					return block;
				}
			}
		}
		return null;
	}

}
