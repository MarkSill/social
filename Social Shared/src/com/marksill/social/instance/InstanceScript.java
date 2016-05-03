package com.marksill.social.instance;

import java.util.Map;

import com.marksill.social.Social;

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
	public boolean running;
	/** The script's thread. */
	public ScriptThread thread;
	public int tabIndex;

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
		code = "print(\"Hello World!\")\n";
		tabIndex = -1;
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (!(this instanceof InstanceClientScript) && Social.getInstance().isServer()) {
			startStop();
		}
	}
	
	public void startStop() {
		if (enabled && !running) {
			running = true;
			thread = new ScriptThread(this, code);
			thread.start();
		} else if (!enabled && running) {
			running = false;
			thread.kill();
		}
	}
	
	@Override
	public void delete() {
		if (thread != null) {
			thread.kill();
		}
		super.delete();
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("enabled", enabled);
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		code = "";
		if (map.get("enabled") != null) {
			enabled = (boolean) map.get("enabled");
		}
	}
	
}
