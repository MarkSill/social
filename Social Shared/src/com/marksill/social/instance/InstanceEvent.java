package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.marksill.social.Social;
import com.marksill.social.networking.NetworkInterface;
import com.marksill.social.networking.RequestClient;

public class InstanceEvent extends Instance {
	
	public static final String CLASS_NAME = "Event";
	
	private List<LuaValue> callbacks;

	public InstanceEvent() {
		super(CLASS_NAME);
	}

	public InstanceEvent(String name) {
		super(name);
	}

	public InstanceEvent(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceEvent(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		callbacks = new ArrayList<>();
	}
	
	public void addCallback(LuaValue callback) {
		callbacks.add(callback);
	}
	
	public void removeCallback(LuaValue callback) {
		callbacks.remove(callback);
	}
	
	public void clearCallbacks() {
		callbacks.clear();
	}
	
	public LuaTable getCallbacks() {
		LuaValue[] values = new LuaValue[callbacks.size()];
		for (int i = 0; i < callbacks.size(); i++) {
			values[i] = callbacks.get(i);
		}
		return LuaTable.listOf(values);
	}
	
	public void fire(Object arg) {
		if (Social.getInstance().isNetworked()) {
			if (Social.getInstance().isServer()) {
				for (LuaValue v : new ArrayList<LuaValue>(callbacks)) {
					v.call(CoerceJavaToLua.coerce(arg));
				}
			} else {
				NetworkInterface client = Social.getInstance().getNetworkInterface();
				Map<String, Object> map = new HashMap<>();
				map.put("id", id);
				map.put("arg", arg);
				RequestClient request = new RequestClient("client", map);
				client.sendUDP(request);
			}
		}
	}

}
