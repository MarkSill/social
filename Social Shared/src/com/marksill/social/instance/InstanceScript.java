package com.marksill.social.instance;

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
		code = "while true do\n" +
		"local instance = Instance:create('block')\ninstance:delete(1500000)\nlocal n1, n2 = math.random(5, 100) / 100, math.random(5, 100) / 100\n" +
		"if math.random(2) == 1 then instance:addShape(Rectangle.new(n1, n2)) else instance:addShape(Circle.new((n1 + n2) / 4)) instance.name = 'circle' end\ninstance.mass = (((n1 + n2) / 2) * 10)^4\n" +
		"instance:setParent(game:findChild('World'))\ninstance.color = Color(math.random(0, 255) / 255, math.random(0, 255) / 255, math.random(0, 255) / 255)\n" +
		"instance.position = Vector2.new(4, 20)\nwait(300)\nend\n";
		tabIndex = -1;
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
	
	@Override
	public void delete() {
		thread.kill();
		super.delete();
	}
	
}
