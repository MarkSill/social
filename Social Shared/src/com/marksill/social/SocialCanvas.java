package com.marksill.social;

import java.awt.AWTEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

public class SocialCanvas extends CanvasGameContainer implements KeyListener {

	private static final long serialVersionUID = 8821660309266958514L;
	
	public boolean added;
	
	private boolean control, alt, shift;

	public SocialCanvas(Game game) throws SlickException {
		super(game);
		added = false;
		control = false;
		alt = false;
		shift = false;
	}

	@Override
	public void setInput(Input input) {
		
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void inputEnded() {
		
	}

	@Override
	public void inputStarted() {
		
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_LALT || key == Input.KEY_RALT) {
			alt = true;
		} else if (key == Input.KEY_LCONTROL || key == Input.KEY_RCONTROL) {
			control = true;
		} else if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) {
			shift = true;
		}
		int mod = 0;
		if (control) {
			mod = mod | InputEvent.CTRL_DOWN_MASK;
		} else if (alt) {
			mod = mod | InputEvent.ALT_DOWN_MASK;
		} else if (shift) {
			mod = mod | InputEvent.SHIFT_DOWN_MASK;
		}
		KeyEvent event = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.nanoTime(), mod, KeyTranslation.toSwing(key), c);
		
		try {
			Class<?> editorClass = Class.forName("com.marksill.social.SocialEditor");
			Method method = editorClass.getDeclaredMethod("dispatchAnEvent", AWTEvent.class);
			JFrame editor = (JFrame) editorClass.getDeclaredField("editor").get(null);
			if (editor != null && method != null) {
				method.invoke(editor, event);
			}
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_LALT || key == Input.KEY_RALT) {
			alt = false;
		} else if (key == Input.KEY_LCONTROL || key == Input.KEY_RCONTROL) {
			control = false;
		} else if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) {
			shift = false;
		}
	}

}
