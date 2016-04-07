package com.marksill.social.lua;

import java.util.List;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.marksill.social.Controllers;

import net.java.games.input.Controller;
import net.java.games.input.Rumbler;

public class LuaControllers extends Controllers {
	
	public static LuaTable controllers() {
		List<Controller> list = Controllers.getControllers();
		LuaValue[] values = new LuaValue[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = CoerceJavaToLua.coerce(list.get(i));
		}
		LuaTable table = LuaTable.listOf(values);
		return table;
	}
	
	public static LuaTable rumblers(Controller controller) {
		Rumbler[] rumblers = controller.getRumblers();
		LuaValue[] values = new LuaValue[rumblers.length];
		for (int i = 0; i < rumblers.length; i++) {
			values[i] = CoerceJavaToLua.coerce(rumblers[i]);
		}
		return LuaTable.listOf(values);
	}

}
