package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.RopeJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.dynamics.joint.WheelJoint;
import org.dyn4j.geometry.Vector2;

import com.marksill.social.Social;

public class InstanceJoints extends Instance {
	
	public static final String CLASS_NAME = "Joints";
	
	private static List<Joint> joints = new ArrayList<>();
	private List<TempJoint> tempjoints;

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
	
	@Override
	public void init() {
		tempjoints = new ArrayList<>();
	}
	
	@Override
	public void updateVars() {
		super.updateVars();
		if (Social.getInstance().isNetworked() && !Social.getInstance().isServer()) {
			joints.clear();
			for (TempJoint j : tempjoints) {
				joints.add(j.create());
			}
		}
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		List<TempJoint> list = new ArrayList<>();
		for (Joint j : joints) {
			TempJoint temp = new TempJoint();
			temp.body1 = InstanceBlock.getBlockByBody(j.getBody1()).id;
			temp.body2 = InstanceBlock.getBlockByBody(j.getBody2()).id;
			temp.a1 = j.getAnchor1();
			if (j instanceof RopeJoint || j instanceof DistanceJoint) {
				temp.a2 = j.getAnchor2();
				if (j instanceof RopeJoint) {
					temp.type = "rope";
				} else if (j instanceof DistanceJoint) {
					temp.type = "rod";
				}
			} else if (j instanceof RevoluteJoint || j instanceof WeldJoint) {
				if (j instanceof RevoluteJoint) {
					temp.type = "hinge";
				} else if (j instanceof WeldJoint) {
					temp.type = "weld";
				}
			}
			if (!temp.type.equals("joint")) {
				list.add(temp);
			}
		}
		map.put("joints", list);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		tempjoints = (List<TempJoint>) map.get("joints");
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
