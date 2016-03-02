package com.marksill.social;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
		KeyEvent event = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.getExtendedKeyCodeForChar(c), c);
		
		JFrame frame = ((JFrame) SwingUtilities.getWindowAncestor(this));
		for (java.awt.event.KeyListener l : frame.getKeyListeners()) {
			l.keyPressed(event);
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
