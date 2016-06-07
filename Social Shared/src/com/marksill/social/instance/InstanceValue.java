package com.marksill.social.instance;

public class InstanceValue extends Instance {
	
	public static final String CLASS_NAME = "Value";
	
	public Object value;

	public InstanceValue() {
		super(CLASS_NAME);
	}

	public InstanceValue(String name) {
		super(name);
	}

	public InstanceValue(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceValue(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		value = null;
	}

}
