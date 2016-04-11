package com.marksill.social.instance;

public class InstanceClientScript extends InstanceScript {
	
	public static final String CLASS_NAME = "ClientScript";
	
	public InstanceClientScript() {
		super(CLASS_NAME);
	}
	
	public InstanceClientScript(String name) {
		super(name);
	}
	
	public InstanceClientScript(Instance parent) {
		super(CLASS_NAME, parent);
	}
	
	public InstanceClientScript(String name, Instance parent) {
		super(name, parent);
	}

}
