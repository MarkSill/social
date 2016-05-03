package com.marksill.social.lua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class Input {
	
	private static Map<Integer, Map<String, Object>> keyboardCallbacks = new HashMap<>();
	private static Map<String, Map<String, Object>> mouseCallbacks = new HashMap<>();
	private static Map<String, Map<String, Object>> controllerCallbacks = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static void add(String name, InputType type, Object input, LuaValue func) {
		if (type == InputType.KEYBOARD) {
			Map<String, Object> keyboard = keyboardCallbacks.get(input);
			if (keyboard == null) {
				keyboard = new HashMap<>();
				keyboard.put("callbacks", new ArrayList<LuaValue>());
				keyboard.put("names", new ArrayList<String>());
			}
			List<String> names = (List<String>) keyboard.get("names");
			List<LuaValue> funcs = (List<LuaValue>) keyboard.get("callbacks");
			if (!names.contains(name)) {
				names.add(name);
			}
			if (!funcs.contains(func)) {
				funcs.add(func);
			}
			keyboardCallbacks.put((int) input, keyboard);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void fire(InputType type, Object val, Object data) {
		if (type == InputType.KEYBOARD) {
			Map<String, Object> map = keyboardCallbacks.get(val);
			if (map != null) {
				List<LuaValue> vals = (List<LuaValue>) map.get("callbacks");
				for (LuaValue v : vals) {
					v.invoke(CoerceJavaToLua.coerce(data));
				}
			}
		}
	}

}
