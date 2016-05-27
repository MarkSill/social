package com.marksill.social.lua;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class Print extends VarArgFunction {
	
	@Override
	public Varargs invoke(Varargs args) {
		int len = args.narg();
		String str = "";
		for (int i = 0; i < len; i++) {
			LuaValue arg = args.arg(i + 1);
			if (i != 0) {
				str += "	";
			}
			str += arg.toString();
		}
		try {
			Class<?> clazz = Class.forName("com.marksill.social.SocialEditor");
			Method method = clazz.getDeclaredMethod("consolePrint", Object.class);
			JFrame editor = (JFrame) clazz.getDeclaredField("editor").get(null);
			method.invoke(editor, str);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
			if (!(e instanceof ClassNotFoundException)) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
