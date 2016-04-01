package com.marksill.social.instance;

public class InstanceContainer extends Instance {
	
	public static final String CLASS_NAME = "Container";

	public InstanceContainer() {
		super(CLASS_NAME);
	}

	public InstanceContainer(String name) {
		super(name);
	}

	public InstanceContainer(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceContainer(String name, Instance parent) {
		super(name, parent);
	}

}
