package com.marksill.social.instance;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.marksill.social.lua.LuaWait;

/**
 * The class for all server side scripts in the game.
 */
public class InstanceScript extends Instance {
	
	/** The script's class name. */
	public static final String CLASS_NAME = "Script";
	
	/** The status of the script. */
	public boolean enabled;
	/** The script's source code. */
	public String code;
	
	/** The running status of the script. */
	private boolean running;
	/** The script's thread. */
	private ScriptThread thread;

	/**
	 * Creates a new script.
	 */
	public InstanceScript() {
		super(CLASS_NAME);
	}

	/**
	 * Creates a new script.
	 * @param name The name of the script.
	 */
	public InstanceScript(String name) {
		super(name);
	}

	/**
	 * Creates a new script.
	 * @param parent The parent of the script.
	 */
	public InstanceScript(Instance parent) {
		super(CLASS_NAME, parent);
	}

	/**
	 * Creates a new script.
	 * @param name The name of the script.
	 * @param parent The parent of the script.
	 */
	public InstanceScript(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		enabled = true;
		running = false;
		code = "while true do\n" +
		"local instance = Instance:create('block')\n" +
		"instance:addShape(Rectangle.new(0.5, 0.5))\n" +
		"instance:setParent(game:findChild('World'))\n" +
		"instance.position = Vector2.new(10, 10)\nwait(25)\nend\n";
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (enabled && !running) {
			running = true;
			thread = new ScriptThread(this, code);
			thread.start();
		} else if (!enabled && running) {
			running = false;
			thread.kill();
		}
	}

}

/**
 * A class for running scripts in their own threads.
 */
class ScriptThread extends Thread {
	
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
		chunk = g.load(code);
	}
	
	@Override
	public void run() {
		chunk.call();
	}
	
	/**
	 * Stops a script immediately.
	 */
	@SuppressWarnings("deprecation")
	public void kill() {
		stop(); //Seems to be the only way to kill a LuaValue....
	}
	
}
