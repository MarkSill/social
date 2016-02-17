package com.marksill.social.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class LuaWait extends OneArgFunction {

	@Override
	public LuaValue call(LuaValue time) {
		try {
			Thread.sleep(time.tolong());
			return LuaValue.TRUE;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return LuaValue.FALSE;
	}

}
