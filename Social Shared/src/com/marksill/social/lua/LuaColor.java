package com.marksill.social.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.newdawn.slick.Color;

public class LuaColor extends ThreeArgFunction {

	@Override
	public LuaValue call(LuaValue r, LuaValue g, LuaValue b) {
		Color color = new Color(r.tofloat(), g.tofloat(), b.tofloat());
		return CoerceJavaToLua.coerce(color);
	}

}
