package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.marksill.social.Social;
import com.marksill.social.lua.Convert;
import com.marksill.social.lua.FireEvent;
import com.marksill.social.networking.NetworkInterface;
import com.marksill.social.networking.RequestClient;

public class InstanceEvent extends Instance {
	
	public static final String CLASS_NAME = "Event";
	
	public FireEvent fire;
	
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
		fire = new FireEvent();
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
	
	@SuppressWarnings("unchecked")
	public void fireBE(Object arg) {
		if (Social.getInstance().isNetworked()) {
			if (Social.getInstance().isServer()) {
				if (arg instanceof Map<?, ?>) {
					arg = Convert.toTable((Map<String, Object>) arg);
				} else if (arg instanceof List<?>) {
					arg = Convert.fromList((List<Object>) arg);
				}
				Varargs varg;
				if (!(arg instanceof Varargs)) {
					varg = CoerceJavaToLua.coerce(arg);
				} else {
					varg = (Varargs) arg;
				}
				for (LuaValue v : new ArrayList<LuaValue>(callbacks)) {
					v.invoke(varg);
				}
			} else {
				arg = Convert.toList((Varargs) arg);
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
