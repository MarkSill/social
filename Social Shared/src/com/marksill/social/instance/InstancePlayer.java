package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import net.java.games.input.Controller;

public class InstancePlayer extends Instance {
	
	public static final String CLASS_NAME = "Player";
	
	private List<LuaValue> keyboardDownCallbacks;
	private List<LuaValue> keyboardUpCallbacks;
	private List<LuaValue> controllerCallbacks;

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
		keyboardDownCallbacks = new ArrayList<LuaValue>();
		keyboardUpCallbacks = new ArrayList<LuaValue>();
		controllerCallbacks = new ArrayList<LuaValue>();
	}
	
	public void addKeyboardDownCallback(LuaValue func) {
		keyboardDownCallbacks.add(func);
	}
	
	public void fireKeyboardDownCallbacks(int key, char c) {
		for (LuaValue v : keyboardDownCallbacks) {
			v.call(LuaValue.valueOf(key), LuaValue.valueOf(c));
		}
	}
	
	public void clearKeyboardDownCallbacks() {
		keyboardDownCallbacks.clear();
	}
	
	public void addKeyboardUpCallback(LuaValue func) {
		keyboardUpCallbacks.add(func);
	}
	
	public void fireKeyboardUpCallbacks(int key, char c) {
		for (LuaValue v : keyboardUpCallbacks) {
			v.call(LuaValue.valueOf(key), LuaValue.valueOf(c));
		}
	}
	
	public void clearKeyboardUpCallbacks() {
		keyboardUpCallbacks.clear();
	}
	
	public void addControllerCallback(LuaValue func) {
		controllerCallbacks.add(func);
	}
	
	public void fireControllerCallbacks(Controller controller, String name, Object data) {
		for (LuaValue v : controllerCallbacks) {
			v.call(CoerceJavaToLua.coerce(controller), LuaValue.valueOf(name), CoerceJavaToLua.coerce(data));
		}
	}
	
	public void clearControllerCallbacks() {
		controllerCallbacks.clear();
	}
	
	public void clearCallbacks() {
		clearKeyboardDownCallbacks();
		clearKeyboardUpCallbacks();
		clearControllerCallbacks();
	}

}
