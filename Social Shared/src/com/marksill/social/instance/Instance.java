package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import com.marksill.social.state.NotGameState;

public class Instance implements Cloneable {
	
	public static final String CLASS_NAME = "Instance";
	
	public static InstanceGame game = null;
	
	private Instance parent;
	public String name;
	
	private List<Instance> children;

	public Instance() {
		this(CLASS_NAME);
	}
	
	public Instance(String name) {
		this(name, null);
	}
	
	public Instance(Instance parent) {
		this(CLASS_NAME, parent);
	}
	
	public Instance(String name, Instance parent) {
		this.name = name;
		this.setParent(parent);
		children = new ArrayList<Instance>();
		NotGameState.instances.add(this);
		init();
	}
	
	public void init() {
		
	}
	
	public void update(int delta) {
		
	}
	
	public void delete() {
		setParent(null);
		NotGameState.instances.remove(this);
	}
	
	public void delete(long time) {
		new Thread() {
			@Override
			public void run() {
				try {
					wait(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				delete();
			}
		}.start();
	}
	
	public List<Instance> getChildren() {
		return children;
	}
	
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
			NotGameState.instances.add(inst);
			return inst;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void removeChild(Instance child) {
		children.remove(child);
	}
	
	public void addChild(Instance child) {
		children.add(child);
	}
	
	public void clearChildren() {
		clearChildren(new ArrayList<Object>());
	}
	
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
	
	public Instance getParent() {
		return parent;
	}

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
	protected void finalize() {
		//STOOF GOES HERE
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + name;
	}
	
	public static Instance create(String className) {
		return create(className, className);
	}
	
	public static Instance create(String className, String name) {
		return create(className, name, null);
	}
	
	public static Instance create(String className, Instance parent) {
		return create(className, className, parent);
	}
	
	public static Instance create(String className, String name, Instance parent) {
		return create(className, parent, name);
	}
	
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
			instance = new InstanceScript(name, parent);
			break;
		case "instance": default:
			instance = new Instance(name, parent);
			break;
		}
		return instance;
	}

}
