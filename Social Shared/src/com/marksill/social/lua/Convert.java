package com.marksill.social.lua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class Convert {
	
	public static LuaTable toTable(Map<String, Object> map) {
		LuaTable tbl = LuaTable.tableOf();
		toTable(tbl, map);
		return tbl;
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
			Object v = null;
			if (val.istable()) {
				v = fromTable((LuaTable) val);
			} else if (val.isboolean()) {
				v = val.checkboolean();
			} else if (val.isnumber()) {
				v = val.checkdouble();
			} else if (val.isstring()) {
				v = val.checkjstring();
			}
			map.put(k, v);
		}
		return map;
	}
	
	public static Varargs fromList(List<Object> list) {
		LuaValue[] values = new LuaValue[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			values[i] = CoerceJavaToLua.coerce(o);
		}
		return LuaValue.varargsOf(values);
	}
	
	public static List<Object> toList(Varargs args) {
		List<Object> list = new ArrayList<>();
		for (int i = 1; i <= args.narg(); i++) {
			LuaValue arg = args.arg(i);
			Object o = null;
			if (arg.istable()) {
				o = fromTable((LuaTable) arg);
			} else if (arg.isboolean()) {
				o = arg.checkboolean();
			} else if (arg.isnumber()) {
				o = arg.checkdouble();
			} else if (arg.isstring()) {
				o = arg.checkjstring();
			}
			list.add(o);
		}
		return list;
	}

}
