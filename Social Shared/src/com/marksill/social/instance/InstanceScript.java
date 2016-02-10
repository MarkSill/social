package com.marksill.social.instance;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.marksill.social.lua.CallOrphanedThread;

public class InstanceScript extends Instance {
	
	public boolean enabled;
	
	private boolean running;
	private Globals g;
	private LuaValue chunk;

	public InstanceScript() {
		super();
	}

	public InstanceScript(String name) {
		super(name);
	}

	public InstanceScript(Instance parent) {
		super(parent);
	}

	public InstanceScript(String name, Instance parent) {
		super(name, parent);
		enabled = true;
	}
	
	@Override
	public void init() {
		enabled = true;
		running = false;
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (enabled && !running) {
			running = true;
			g = JsePlatform.debugGlobals();
			g.set("script", CoerceJavaToLua.coerce(this));
			g.set("kill", new CallOrphanedThread());
			chunk = g.loadfile("src/lua/multithread.lua");
			chunk.call();
			g.get("start").call(LuaValue.valueOf("print('hi')"));
		} else if (!enabled && running) {
			running = false;
			g.get("stop").call();
		}
	}

}
