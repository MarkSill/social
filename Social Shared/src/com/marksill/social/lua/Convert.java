package com.marksill.social.lua;

import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class Convert {
	
	public static LuaValue toTable(Map<String, Object> map) {
		LuaTable val = LuaTable.tableOf();
		toTable(val, map);
		return val;
	}
	
	@SuppressWarnings("unchecked")
	private static void toTable(LuaTable tbl, Map<String, Object> map) {
		for (String k : map.keySet()) {
			Object v = map.get(k);
			if (v instanceof Map) {
				LuaTable val = LuaTable.tableOf();
				toTable(val, (Map<String, Object>) v);
				v = val;
			}
			tbl.set(k, CoerceJavaToLua.coerce(v));
		}
	}
	
	public static Map<String, Object> fromTable(LuaTable tbl) {
		LuaValue[] keys = tbl.keys();
		Map<String, Object> map = new HashMap<String, Object>();
		for (LuaValue key : keys) {
			String k = key.checkjstring();
			LuaValue val = tbl.get(key);
			Object v;
			if (val.istable()) {
				v = fromTable((LuaTable) val);
			} else if (val.isboolean()) {
				v = val.checkboolean();
			} else if (val.isnumber()) {
				v = val.checkdouble();
			} else if (val.isstring()) {
				v = val.checkjstring();
			} else {
				v = null;
			}
			map.put(k, v);
		}
		return map;
	}

}
