package com.marksill.social.instance;

public class InstanceGame extends Instance {
	
	public static final String CLASS_NAME = "Game";

	public InstanceGame() {
		super(CLASS_NAME);
	}

	public InstanceGame(String name) {
		super(name);
	}

	public InstanceGame(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceGame(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		Instance.game = this;
		new InstanceWorld().parent = this;
	}

}
