package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import com.marksill.social.state.NotGameState;

/**
 * The base class for all instances.
 */
public class Instance implements Cloneable {
	
	/** Instance's class name. */
	public static final String CLASS_NAME = "Instance";
	
	/** The instance of the game. */
	public static InstanceGame game = null;
	
	/** The instance's parent. */
	private Instance parent;
	/** The name of the instance. */
	public String name;
	
	/** The instance's children. */
	private List<Instance> children;

	/**
	 * Creates a new Instance.
	 */
	public Instance() {
		this(CLASS_NAME);
	}
	
	/**
	 * Creates a new Instance.
	 * @param name The name of the instance.
	 */
	public Instance(String name) {
		this(name, null);
	}
	
	/**
	 * Creates a new Instance.
	 * @param parent The parent of the instance.
	 */
	public Instance(Instance parent) {
		this(CLASS_NAME, parent);
	}
	
	/**
	 * Creates a new Instance.
	 * @param name The name of the instance.
	 * @param parent The parent of the instance.
	 */
	public Instance(String name, Instance parent) {
		this.name = name;
		this.setParent(parent);
		children = new ArrayList<Instance>();
		NotGameState.addInstance(this);
		init();
	}
	
	/**
	 * Initializes the instance.
	 */
	public void init() {
		
	}
	
	/**
	 * Updates the instance.
	 * @param delta The time since the last update in milliseconds.
	 */
	public void update(int delta) {
		
	}
	
	/**
	 * Deletes the instance.
	 */
	public void delete() {
		setParent(null);
		NotGameState.removeInstance(this);
	}
	
	/**
	 * Deletes the instance in the given amount of time.
	 * @param time The amount of time (in milliseconds) to wait before deleting.
	 */
	public void delete(long time) {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				delete();
			}
		}.start();
	}
	
	/**
	 * Gets the instance's children.
	 * @return The instance's children.
	 */
	public List<Instance> getChildren() {
		return children;
	}
	
	/**
	 * Finds a child of the instance.
	 * @param name The name of the child.
	 * @return The child with the given name (or null).
	 */
	public Instance findChild(String name) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).name.equals(name)) {
				return children.get(i);
			}
		}
		return null;
	}
	
	@Override
	public Instance clone() {
		try {
			Instance inst = (Instance) super.clone();
			NotGameState.addInstance(inst);
			return inst;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Removes a child from this instance.
	 * @param child The child to remove.
	 */
	public void removeChild(Instance child) {
		children.remove(child);
	}
	
	/**
	 * Adds a child to this instance.
	 * @param child The child to add.
	 */
	public void addChild(Instance child) {
		children.add(child);
	}
	
	/**
	 * Removes all children from this instance.
	 */
	public void clearChildren() {
		clearChildren(new ArrayList<Object>());
	}
	
	/**
	 * Removes all children from this instance that don't match the given exceptions.<br/>
	 * The list of exceptions should be a List<Object>. It should contain Instances and Strings.<br/>
	 * Strings are the names of instances that should be exempt from removal, whereas Instances are the actual instances to be spared.
	 * @param exceptions The list of exceptions to removal.
	 */
	public void clearChildren(List<Object> exceptions) {
		for (int i = 0; i < children.size(); i++) {
			Instance inst = children.get(i);
			boolean found = false;
			for (Object o : exceptions) {
				if (o instanceof Instance) {
					if (inst == o) {
						found = true;
					}
				} else if (o instanceof String) {
					if (inst.name.equals(o)) {
						found = true;
					}
				}
			}
			if (!found) {
				children.remove(inst);
				i--;
			}
		}
	}
	
	/**
	 * Gets this instance's parent.
	 * @return The instance's parent.
	 */
	public Instance getParent() {
		return parent;
	}

	/**
	 * Sets this instance's parent.
	 * @param parent The instance's new parent.
	 */
	public void setParent(Instance parent) {
		if (this.parent != null) {
			this.parent.removeChild(this);
		}
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	@Override
	protected void finalize() {}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + name;
	}
	
	/**
	 * Creates a new instance.
	 * @param className The class name of the instance to create.
	 * @return The created instance.
	 */
	public static Instance create(String className) {
		return create(className, className);
	}
	
	/**
	 * Creates a new instance.
	 * @param className The class name of the instance to create.
	 * @param name The name of the instance.
	 * @return The created instance.
	 */
	public static Instance create(String className, String name) {
		return create(className, name, null);
	}
	
	/**
	 * Creates a new instance.
	 * @param className The class name of the instance to create.
	 * @param parent The parent of the instance.
	 * @return The created instance.
	 */
	public static Instance create(String className, Instance parent) {
		return create(className, className, parent);
	}
	
	/**
	 * Creates a new instance.
	 * @param className The class name of the instance to create.
	 * @param name The name of the instance.
	 * @param parent The parent of the instance.
	 * @return The created instance.
	 */
	public static Instance create(String className, String name, Instance parent) {
		return create(className, parent, name);
	}
	
	/**
	 * Creates a new instance.
	 * @param className The class name of the instance to create.
	 * @param parent The parent of the instance.
	 * @param name The name of the instance.
	 * @return The created instance.
	 */
	public static Instance create(String className, Instance parent, String name) {
		Instance instance = null;
		className = className.toLowerCase();
		switch (className) {
		case "game":
			instance = new InstanceGame(name, parent);
			break;
		case "world":
			instance = new InstanceWorld(name, parent);
			break;
		case "script":
			instance = new InstanceScript(name, parent);
			break;
		case "block":
			instance = new InstanceBlock(name, parent);
			break;
		case "instance": default:
			instance = new Instance(name, parent);
			break;
		}
		return instance;
	}

}
