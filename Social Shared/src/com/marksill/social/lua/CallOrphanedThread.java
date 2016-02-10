package com.marksill.social.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.OrphanedThread;
import org.luaj.vm2.lib.ZeroArgFunction;

public class CallOrphanedThread extends ZeroArgFunction {

	@Override
	public LuaValue call() {
		try {
			throw new OrphanedThread();
		} catch (OrphanedThread e) {
			
		}
		return null;
	}

}
