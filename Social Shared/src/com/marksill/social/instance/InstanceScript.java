package com.marksill.social.instance;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.marksill.social.lua.LuaColor;
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
		"local instance = Instance:create('block')\ninstance:delete(1500000)\n" +
		"instance:addShape(Rectangle.new(math.random(5, 100) / 100, math.random(5, 100) / 100))\n" +
		"instance:setParent(game:findChild('World'))\ninstance.color = Color(math.random(0, 255) / 255, math.random(0, 255) / 255, math.random(0, 255) / 255)\n" +
		"instance.position = Vector2.new(10, 10)\nwait(300)\nend\n";
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
