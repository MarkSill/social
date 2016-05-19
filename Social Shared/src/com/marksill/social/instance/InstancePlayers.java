package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class InstancePlayers extends Instance {
	
	public static final String CLASS_NAME = "Players";
	
	public int maxPlayers = 4;
	
	private List<LuaValue> playerAddedCallbacks;
	private List<LuaValue> playerRemovedCallbacks;

	public InstancePlayers() {
		super(CLASS_NAME);
	}
	
	public InstancePlayers(String name) {
		super(name);
	}
	
	public InstancePlayers(Instance parent) {
		super(CLASS_NAME, parent);
	}
	
	public InstancePlayers(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		playerAddedCallbacks = new ArrayList<>();
		playerRemovedCallbacks = new ArrayList<>();
	}
	
	public LuaTable getPlayers() {
		List<InstancePlayer> playerList = getPlayersAsList();
		LuaValue[] values = new LuaValue[playerList.size()];
		for (int i = 0; i < playerList.size(); i++) {
			values[i] = CoerceJavaToLua.coerce(playerList.get(i));
		}
		return LuaTable.listOf(values);
	}
	
	public List<InstancePlayer> getPlayersAsList() {
		List<InstancePlayer> list = new ArrayList<InstancePlayer>();
		for (Instance i : getChildren()) {
			if (i instanceof InstancePlayer) {
				list.add((InstancePlayer) i);
			}
		}
		return list;
	}
	
	public void addPlayer(InstancePlayer player) {
		if (getPlayers().length() < maxPlayers) {
			player.setParent(this);
			
			for (LuaValue v : playerAddedCallbacks) {
				v.call(CoerceJavaToLua.coerce(player));
			}
		} else {
			player.delete();
		}
	}
	
	public void removePlayer(InstancePlayer player) {
		for (LuaValue v : playerRemovedCallbacks) {
			v.call(CoerceJavaToLua.coerce(player));
		}
		player.delete();
	}
	
	public void addPlayerAddedCallback(LuaValue v) {
		playerAddedCallbacks.add(v);
	}
	
	public void addPlayerRemovedCallback(LuaValue v) {
		playerRemovedCallbacks.add(v);
	}
	
	public void clearCallbacks() {
		playerAddedCallbacks.clear();
		playerRemovedCallbacks.clear();
	}
	
	@Override
	public Map<String, Object> createMap() {
		Map<String, Object> map = super.createMap();
		map.put("maxPlayers", maxPlayers);
		return map;
	}
	
	@Override
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		if (map.get("maxPlayers") != null) {
			maxPlayers = (int) map.get("maxPlayers");
		}
	}

}
