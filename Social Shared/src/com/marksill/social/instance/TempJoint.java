package com.marksill.social.instance;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.RopeJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;

public class TempJoint {
	
	public String type;
	public long body1, body2;
	public Vector2 a1, a2;

	public TempJoint() {
		type = "joint";
		body1 = -1;
		body2 = -1;
		a1 = new Vector2();
		a2 = new Vector2();
	}
	
	public Joint create() {
		Joint joint = null;
		Body b1 = null;
		Body b2 = null;
		if (body1 != -1) {
			b1 = ((InstanceBlock) Instance.getByID(body1)).getBody();
			b2 = ((InstanceBlock) Instance.getByID(body2)).getBody();
		}
		switch(type) {
		case "angle":
			break;
		case "rod":
			joint = new DistanceJoint(b1, b2, a1, a2);
			break;
		case "motor":
			break;
		case "pin":
			break;
		case "prismatic":
			break;
		case "pulley":
			break;
		case "hinge":
			joint = new RevoluteJoint(b1, b2, a1);
			break;
		case "rope":
			joint = new RopeJoint(b1, b2, a1, a2);
			break;
		case "weld":
			joint = new WeldJoint(b1, b2, a1);
			break;
		case "wheel":
			break;
		}
		return joint;
	}

}
