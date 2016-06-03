package com.marksill.social.lua;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.marksill.social.instance.InstanceEvent;

public class FireEvent extends VarArgFunction {
	
	public FireEvent() {
		super();
	}
	
	@Override
	public Varargs invoke(Varargs args) {
		InstanceEvent event = null;
		event = (InstanceEvent) CoerceLuaToJava.coerce(args.arg1(), InstanceEvent.class);
		args = args.subargs(2);
		event.fireBE(args);
		return null;
	}

}
