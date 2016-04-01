package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaValue;

public class InstancePlayer extends Instance {
	
	public static final String CLASS_NAME = "Player";
	
	private List<LuaValue> inputCallbacks;

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
	
	@Override
	public void init() {
		inputCallbacks = new ArrayList<LuaValue>();
	}
	
	public void addInputCallback(LuaValue func) {
		inputCallbacks.add(func);
	}
	
	public void fireInputCallback(int key, char c) {
		for (LuaValue v : inputCallbacks) {
			v.call(LuaValue.valueOf(key), LuaValue.valueOf(c));
		}
	}

}
