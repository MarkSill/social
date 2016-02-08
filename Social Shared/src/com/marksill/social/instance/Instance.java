package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import com.marksill.social.state.NotGameState;

public class Instance implements Cloneable {
	
	public static final String CLASS_NAME = "Instance";
	
	public static InstanceGame game = null;
	
	public Instance parent;
	public String name;
	public boolean initialized;
	
	private Instance oldParent;
	
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
		this.parent = parent;
		children = new ArrayList<Instance>();
		NotGameState.instances.add(this);
		initialized = false;
	}
	
	public void init() {
		
	}
	
	public void update(int delta) {
		if (oldParent != parent) {
			if (oldParent != null) {
				oldParent.removeChild(this);
			}
			if (parent != null) {
				parent.addChild(this);
			}
			oldParent = parent;
		}
		if (!initialized) {
			init();
			initialized = true;
		}
	}
	
	public void delete() {
		parent = null;
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
	public Object clone() {
		try {
			Instance inst = (Instance) super.clone();
			NotGameState.instances.add(inst);
			inst.initialized = false;
			inst.init();
			inst.oldParent = null;
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
	
	@Override
	protected void finalize() {
		//STOOF GOES HERE
	}

}
