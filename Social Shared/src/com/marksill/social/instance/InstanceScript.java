package com.marksill.social.instance;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public class InstanceScript extends Instance {
	
	public boolean enabled;
	public String code;
	
	private boolean running;
	private SpecialThread thread;

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
	}
	
	@Override
	public void init() {
		enabled = true;
		running = false;
		code = "";
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (enabled && !running) {
			running = true;
			thread = new SpecialThread(this, code);
			thread.start();
		} else if (!enabled && running) {
			running = false;
			thread.kill();
		}
	}

}

class SpecialThread extends Thread {
	
	Globals g;
	LuaValue chunk;
	String code;
	InstanceScript script;
	
	public SpecialThread(InstanceScript script, String code) {
		g = JsePlatform.debugGlobals();
		this.code = code;
		this.script = script;
		g.set("script", CoerceJavaToLua.coerce(script));
		chunk = g.load(code);
	}
	
	@Override
	public void run() {
		chunk.call();
	}
	
	@SuppressWarnings("deprecation")
	public void kill() {
		stop(); //Seems to be the only way to kill a LuaValue....
	}
	
}
