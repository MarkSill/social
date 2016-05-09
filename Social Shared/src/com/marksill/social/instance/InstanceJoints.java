package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.RopeJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.dynamics.joint.WheelJoint;
import org.dyn4j.geometry.Vector2;

public class InstanceJoints extends Instance {
	
	public static final String CLASS_NAME = "Joints";
	
	private static List<Joint> joints = new ArrayList<>();

	public InstanceJoints() {
		super(CLASS_NAME);
	}
	
	public InstanceJoints(String name) {
		super(name);
	}
	
	public InstanceJoints(Instance parent) {
		super(CLASS_NAME, parent);
	}
	
	public InstanceJoints(String name, Instance parent) {
		super(name, parent);
	}
	
	public static DistanceJoint createRod(InstanceBlock b1, InstanceBlock b2, Vector2 a1, Vector2 a2) {
		DistanceJoint joint = new DistanceJoint(b1.getBody(), b2.getBody(), a1, a2);
		joints.add(joint);
		((InstanceWorld) Instance.game.findChild("World")).getWorld().addJoint(joint);
		return joint;
	}
	
	public static RevoluteJoint createHinge(InstanceBlock b1, InstanceBlock b2, Vector2 pos) {
		RevoluteJoint joint = new RevoluteJoint(b1.getBody(), b2.getBody(), pos);
		joints.add(joint);
		((InstanceWorld) Instance.game.findChild("World")).getWorld().addJoint(joint);
		return joint;
	}
	
	public static WeldJoint createWeld(InstanceBlock b1, InstanceBlock b2, Vector2 pos) {
		WeldJoint joint = new WeldJoint(b1.getBody(), b2.getBody(), pos);
		joints.add(joint);
		((InstanceWorld) Instance.game.findChild("World")).getWorld().addJoint(joint);
		return joint;
	}
	
	public static RopeJoint createRope(InstanceBlock b1, InstanceBlock b2, Vector2 a1, Vector2 a2) {
		RopeJoint joint = new RopeJoint(b1.getBody(), b2.getBody(), a1, a2);
		joint.setLowerLimit(0);
		joint.setCollisionAllowed(true);
		joints.add(joint);
		((InstanceWorld) Instance.game.findChild("World")).getWorld().addJoint(joint);
		return joint;
	}
	
	public static WheelJoint createWheel(InstanceBlock b1, InstanceBlock b2, Vector2 anchor, Vector2 axis) {
		WheelJoint joint = new WheelJoint(b1.getBody(), b2.getBody(), anchor, axis);
		joints.add(joint);
		((InstanceWorld) Instance.game.findChild("World")).getWorld().addJoint(joint);
		return joint;
	}
	
	public static List<Joint> getJointsAsList() {
		return new ArrayList<Joint>(joints);
	}
	
	public static void removeAllJoints() {
		InstanceWorld world = (InstanceWorld) Instance.game.findChild("World");
		if (world != null) {
			world.getWorld().removeAllJoints();
		}
		joints.clear();
	}

}
