package com.marksill.social.instance;

import java.util.Map;

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
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("code", code);
		return map;
	}

}
