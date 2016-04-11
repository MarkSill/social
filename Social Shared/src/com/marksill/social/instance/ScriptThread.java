package com.marksill.social.instance;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.lwjgl.input.Keyboard;

import com.marksill.social.lua.LuaColor;
import com.marksill.social.lua.LuaControllers;
import com.marksill.social.lua.LuaWait;

/**
 * A class for running scripts in their own threads.
 */
public class ScriptThread extends Thread {
	
	/** The script's Lua Globals. */
	Globals g;
	/** The script's loaded code. */
	LuaValue chunk;
	/** The script's code as a String. */
	String code;
	/** The script that the thread was created by. */
	InstanceScript script;
	
	/**
	 * Creates a new thread for a script.
	 * @param script The script that the thread was created by.
	 * @param code The script's code.
	 */
	public ScriptThread(InstanceScript script, String code) {
		g = JsePlatform.debugGlobals();
		this.code = code;
		this.script = script;
		g.set("script", CoerceJavaToLua.coerce(script));
		g.set("game", CoerceJavaToLua.coerce(Instance.game));
		g.set("Instance", CoerceJavaToLua.coerce(Instance.class));
		g.set("Rectangle", CoerceJavaToLua.coerce(Rectangle.class));
		g.set("Circle", CoerceJavaToLua.coerce(Circle.class));
		g.set("Vector2", CoerceJavaToLua.coerce(Vector2.class));
		g.set("wait", CoerceJavaToLua.coerce(new LuaWait()));
		g.set("Color", CoerceJavaToLua.coerce(new LuaColor()));
		if (script instanceof InstanceClientScript) {
			g.set("Keyboard", CoerceJavaToLua.coerce(Keyboard.class));
			g.set("Controllers", CoerceJavaToLua.coerce(LuaControllers.class));
		}
		chunk = g.load(code);
	}
	
	@Override
	public void run() {
		if (chunk != null) {
			chunk.call();
		}
	}
	
	/**
	 * Stops a script immediately.
	 */
	@SuppressWarnings("deprecation")
	public void kill() {
		stop(); //Seems to be the only way to kill a LuaValue....
	}
	
}
