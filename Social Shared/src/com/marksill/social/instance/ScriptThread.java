package com.marksill.social.instance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.lwjgl.input.Keyboard;

import com.marksill.social.lua.GetPlayer;
import com.marksill.social.lua.LuaColor;
import com.marksill.social.lua.LuaControllers;
import com.marksill.social.lua.LuaWait;
import com.marksill.social.lua.Print;

/**
 * A class for running scripts in their own threads.
 */
public class ScriptThread extends Thread {
	
	public static Pattern errorPattern = Pattern.compile("(\\[.*\\]:)");
	public static Pattern errorPattern2 = Pattern.compile("(?<=.*: ).*");
	
	/** The script's Lua Globals. */
	Globals g;
	/** The script's loaded code. */
	LuaValue chunk;
	/** The script's code as a String. */
	String code;
	/** The script that the thread was created by. */
	InstanceScript script;
	
	/**
	 * Creates a new thread for a script.
	 * @param script The script that the thread was created by.
	 * @param code The script's code.
	 */
	public ScriptThread(InstanceScript script, String code) {
		Thread.currentThread().setName("Script");
		this.code = code;
		this.script = script;
	}
	
	@Override
	public void run() {
		g = JsePlatform.debugGlobals();
		g.set("script", CoerceJavaToLua.coerce(script));
		g.set("game", CoerceJavaToLua.coerce(Instance.game));
		g.set("Instance", CoerceJavaToLua.coerce(Instance.class));
		g.set("Rectangle", CoerceJavaToLua.coerce(Rectangle.class));
		g.set("Circle", CoerceJavaToLua.coerce(Circle.class));
		g.set("Vector2", CoerceJavaToLua.coerce(Vector2.class));
		g.set("wait", CoerceJavaToLua.coerce(new LuaWait()));
		g.set("Color", CoerceJavaToLua.coerce(new LuaColor()));
		g.set("Images", CoerceJavaToLua.coerce(InstanceImages.class));
		g.set("Joints", CoerceJavaToLua.coerce(InstanceJoints.class));
		g.set("print", CoerceJavaToLua.coerce(new Print()));
		if (script instanceof InstanceClientScript) {
			g.set("Keyboard", CoerceJavaToLua.coerce(Keyboard.class));
			g.set("Controllers", CoerceJavaToLua.coerce(LuaControllers.class));
			g.set("player", CoerceJavaToLua.coerce(new GetPlayer()));
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			chunk = g.load(code);
			if (chunk != null) {
				chunk.call();
			}
		} catch (LuaError error) {
			String str = error.getMessageObject().tojstring();
			str = str.replace('\n', ' ');
			Matcher m = errorPattern.matcher(str);
			m.find();
			int end = m.end();
			str = str.substring(end);
			m = errorPattern2.matcher(str);
			if (m.find()) {
				int start = m.start();
				String str2 = str.substring(start);
				str = str.substring(0, start);
				str = "Line " + str + "\"" + str2 + "\" in script \"" + script.name + "\"";
				try {
					Class<?> clazz = Class.forName("com.marksill.social.SocialEditor");
					Method method = clazz.getDeclaredMethod("consolePrint", Object.class, boolean.class);
					JFrame editor = (JFrame) clazz.getDeclaredField("editor").get(null);
					if (method != null && editor != null) {
						method.invoke(editor, str, true);
					}
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
					if (!(e instanceof ClassNotFoundException)) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Stops a script immediately.
	 */
	@SuppressWarnings("deprecation")
	public void kill() {
		stop(); //Seems to be the only way to kill a LuaValue....
		interrupt();
	}
	
	public void finalize() {
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		script = null;
		g = null;
		chunk = null;
		code = null;
	}
	
}
