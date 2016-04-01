package com.marksill.social.instance;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class InstancePlayers extends Instance {
	
	public static final String CLASS_NAME = "Players";
	
	public int maxPlayers = 4;

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
	
	public LuaTable getPlayers() {
		List<InstancePlayer> playerList = new ArrayList<InstancePlayer>();
		for (Instance i : getChildren()) {
			if (i instanceof InstancePlayer) {
				playerList.add((InstancePlayer) i);
			}
		}
		LuaValue[] values = new LuaValue[playerList.size()];
		for (int i = 0; i < playerList.size(); i++) {
			values[i] = CoerceJavaToLua.coerce(playerList.get(i));
		}
		return LuaTable.listOf(values);
	}
	
	public void addPlayer(InstancePlayer player) {
		if (getPlayers().length() < maxPlayers) {
			player.setParent(this);
		} else {
			player.delete();
		}
	}

}
