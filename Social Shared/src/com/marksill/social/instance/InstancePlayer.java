package com.marksill.social.instance;

public class InstancePlayer extends Instance {
	
	public static final String CLASS_NAME = "Player";

	public InstancePlayer() {
		super(CLASS_NAME);
	}

	public InstancePlayer(String name) {
		super(name);
	}

	public InstancePlayer(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstancePlayer(String name, Instance parent) {
		super(name, parent);
	}

}
