package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.TreeNode;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.marksill.social.Social;
import com.marksill.social.state.NotGameState;

/**
 * The base class for all instances.
 */
public class Instance implements Cloneable {
	
	/** Instance's class name. */
	public static final String CLASS_NAME = "Instance";
	
	public static List<Instance> selected = new ArrayList<Instance>();
	private static List<Instance> instances = new ArrayList<Instance>();
	
	/** The instance of the game. */
	public static InstanceGame game = null;
	
	private static long nextID = 0;
	
	/** The instance's parent. */
	private Instance parent;
	/** The name of the instance. */
	public String name;
	public long id;
	
	/** The instance's children. */
	protected List<Instance> children;
	
	public TreeNode node;

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
		instances.add(this);
		this.name = name;
		this.setParent(parent);
		children = new ArrayList<>();
		NotGameState.addInstance(this);
		if (Social.getInstance().isNetworked() && Social.getInstance().isServer()) {
			id = nextID++;
		}
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
	
	public void updateVars() {
		
	}
	
	/**
	 * Deletes the instance.
	 */
	public void delete() {
		setParent(null);
		NotGameState.removeInstance(this);
		instances.remove(this);
		node = null;
		for (Instance i : new ArrayList<>(children)) {
			i.delete();
		}
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
		return new ArrayList<Instance>(children);
	}
	
	public LuaTable children() {
		LuaValue[] values = new LuaValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			values[i] = CoerceJavaToLua.coerce(children.get(i));
		}
		return LuaTable.listOf(values);
	}
	
	/**
	 * Finds a child of the instance.
	 * @param name The name of the child.
	 * @return The child with the given name (or null).
	 */
	public Instance findChild(String name) {
		for (Instance child : getChildren()) {
			if (child.name.equals(name)) {
				return child;
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
		clearChildren(new ArrayList<>());
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
	
	public boolean childOf(Instance other) {
		if (other == null) {
			return false;
		}
		for (Instance i : new ArrayList<Instance>(other.children)) {
			if (i == this || childOf(i)) {
				return true;
			}
		}
		return false;
	}
	
	public Map<String, Object> createMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("cname", getClass().getName());
		map.put("name", name);
		List<Long> list = new ArrayList<>();
		for (Instance i : new ArrayList<Instance>(children)) {
			list.add(i.id);
		}
		map.put("children", list);
		map.put("id", id);
		return map;
	}
	
	public void loadFromMap(Map<String, Object> map) {
		if (map.get("id") != null) {
			id = (long) map.get("id");
		}
		if (map.get("name") != null) {
			name = (String) map.get("name");
		}
	}

	@Override
	protected void finalize() {}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + name + ": id=" + id + ", children=" + children;
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
		case "instance": default:
			instance = new Instance(name, parent);
			break;
		case "block":
			instance = new InstanceBlock(name, parent);
			break;
		case "circle":
			instance = new InstanceCircle(name, parent);
			break;
		case "clientscript":
			instance = new InstanceClientScript(name, parent);
			break;
		case "container":
			instance = new InstanceContainer(name, parent);
			break;
		case "game":
			instance = new InstanceGame(name, parent);
			break;
		case "rectangle":
			instance = new InstanceRectangle(name, parent);
			break;
		case "script":
			instance = new InstanceScript(name, parent);
			break;
		case "world":
			instance = new InstanceWorld(name, parent);
			break;
		}
		return instance;
	}
	
	public static List<Instance> findInstances(Instance parent, Class<? extends Instance> clazz) {
		List<Instance> list = new ArrayList<>();
		if (parent.getClass().equals(clazz)) {
			list.add(parent);
		}
		for (Instance i : parent.getChildren()) {
			for (Instance inst : findInstances(i, clazz)) {
				list.add(inst);
			}
		}
		return list;
	}
	
	public static Instance getByID(long id) {
		for (Instance i : instances) {
			if (i.id == id) {
				return i;
			}
		}
		return null;
	}
	
	public static Instance getByID(long id, Instance parent) {
		if (parent == null) {
			return null;
		}
		if (parent.id == id) {
			return parent;
		}
		for (Instance i : parent.children) {
			Instance inst = getByID(id, i);
			if (inst != null) {
				return inst;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void fromMap(Map<Long, Map<String, Object>> map) {
		Set<Long> ids = map.keySet();
		for (Long id : ids) {
			Map<String, Object> obj = map.get(id);
			Instance inst = getByID(id);
			if (inst == null) {
				try {
					inst = (Instance) Class.forName((String) obj.get("cname")).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			inst.loadFromMap(obj);
			inst.id = id;
		}
		for (Long id : ids) {
			Map<String, Object> obj = map.get(id);
			List<Long> cids = (List<Long>) obj.get("children");
			if (cids != null) {
				for (Long nid : cids) {
					Instance inst = getByID(nid);
					Instance parent = getByID(id);
					if (inst != null && parent != null && inst.parent != parent) {
						inst.setParent(parent);
					}
				}
			}
		}
		deleteIfNoLongerExists(ids);
	}
	
	public static Map<Long, Map<String, Object>> toMap() {
		Map<Long, Map<String, Object>> map = new HashMap<>();
		toMap(map, Instance.game);
		return map;
	}
	
	public static void toMap(Map<Long, Map<String, Object>> map, Instance parent) {
		map.put(parent.id, parent.createMap());
		for (Instance inst : new ArrayList<Instance>(parent.children)) {
			toMap(map, inst);
		}
	}
	
	public static void deleteIfNoLongerExists(Set<Long> ids) {
		List<Instance> list = new ArrayList<>(instances);
		for (Instance i : list) {
			if (!ids.contains(i.id)) {
				if (i instanceof InstanceRectangle) {
					System.out.println(((InstanceRectangle) i).getParent());
				}
				System.out.println("Deleting object " + i + " (ID " + i.id + ")");
				i.delete();
			}
		}
	}

}
