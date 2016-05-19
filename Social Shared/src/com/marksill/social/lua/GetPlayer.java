package com.marksill.social.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstancePlayer;

public class GetPlayer extends ZeroArgFunction {

	@Override
	public LuaValue call() {
		return CoerceJavaToLua.coerce(Instance.getByID(InstancePlayer.pid));
	}

}
