package com.marksill.social;

import static java.awt.event.KeyEvent.*;
import java.util.HashMap;
import java.util.Map;

import static org.newdawn.slick.Input.*;

public class KeyTranslation {
	
	public static final Map<Integer, Integer> slickToSwing;
	public static final Map<Integer, Integer> swingToSlick;
	
	public static int toSwing(int slick) {
		return slickToSwing.get(slick);
	}
	
	public static int toSlick(int swing) {
		return swingToSlick.get(swing);
	}

	static {
		slickToSwing = new HashMap<Integer, Integer>();
		swingToSlick = new HashMap<Integer, Integer>();
		Integer[] slick = new Integer[] {
				KEY_UNLABELED,
				KEY_0,
				KEY_1,
				KEY_2,
				KEY_3,
				KEY_4,
				KEY_5,
				KEY_6,
				KEY_7,
				KEY_8,
				KEY_9,
				KEY_A,
				KEY_ADD,
				KEY_APOSTROPHE,
				KEY_AT,
				KEY_B,
				KEY_BACK,
				KEY_BACKSLASH,
				KEY_C,
				KEY_CAPITAL,
				KEY_COLON,
				KEY_COMMA,
				KEY_D,
				KEY_DECIMAL,
				KEY_DELETE,
				KEY_DIVIDE,
				KEY_DOWN,
				KEY_E,
				KEY_END,
				KEY_ENTER,
				KEY_EQUALS,
				KEY_ESCAPE,
				KEY_F,
				KEY_F1,
				KEY_F10,
				KEY_F11,
				KEY_F12,
				KEY_F13,
				KEY_F14,
				KEY_F15,
				KEY_F2,
				KEY_F3,
				KEY_F4,
				KEY_F5,
				KEY_F6,
				KEY_F7,
				KEY_F8,
				KEY_F9,
				KEY_G,
				KEY_GRAVE,
				KEY_H,
				KEY_HOME,
				KEY_I,
				KEY_INSERT,
				KEY_J,
				KEY_K,
				KEY_L,
				KEY_LALT,
				KEY_LBRACKET,
				KEY_LCONTROL,
				KEY_LEFT,
				KEY_LMENU,
				KEY_LSHIFT,
				KEY_LWIN,
				KEY_M,
				KEY_MINUS,
				KEY_MULTIPLY,
				KEY_N,
				KEY_NUMLOCK,
				KEY_NUMPAD0,
				KEY_NUMPAD1,
				KEY_NUMPAD2,
				KEY_NUMPAD3,
				KEY_NUMPAD4,
				KEY_NUMPAD5,
				KEY_NUMPAD6,
				KEY_NUMPAD7,
				KEY_NUMPAD8,
				KEY_NUMPAD9,
				KEY_NUMPADCOMMA,
				KEY_NUMPADENTER,
				KEY_NUMPADEQUALS,
				KEY_O,
				KEY_P,
				KEY_PAUSE,
				KEY_PERIOD,
				KEY_Q,
				KEY_R,
				KEY_RALT,
				KEY_RBRACKET,
				KEY_RCONTROL,
				KEY_RETURN,
				KEY_RIGHT,
				KEY_RMENU,
				KEY_RSHIFT,
				KEY_RWIN,
				KEY_S,
				KEY_SEMICOLON,
				KEY_SLASH,
				KEY_SPACE,
				KEY_SUBTRACT,
				KEY_T,
				KEY_TAB,
				KEY_U,
				KEY_UP,
				KEY_V,
				KEY_W,
				KEY_X,
				KEY_Y,
				KEY_Z
		};
		Integer[] swing = new Integer[] {
				VK_UNDEFINED,
				VK_0,
				VK_1,
				VK_2,
				VK_3,
				VK_4,
				VK_5,
				VK_6,
				VK_7,
				VK_8,
				VK_9,
				VK_A,
				VK_ADD,
				VK_QUOTE,
				VK_AT,
				VK_B,
				VK_BACK_SPACE,
				VK_BACK_SLASH,
				VK_C,
				VK_CAPS_LOCK,
				VK_COLON,
				VK_COMMA,
				VK_D,
				VK_DECIMAL,
				VK_DELETE,
				VK_DIVIDE,
				VK_DOWN,
				VK_E,
				VK_END,
				VK_ENTER,
				VK_EQUALS,
				VK_ESCAPE,
				VK_F,
				VK_F1,
				VK_F10,
				VK_F11,
				VK_F12,
				VK_F13,
				VK_F14,
				VK_F15,
				VK_F2,
				VK_F3,
				VK_F4,
				VK_F5,
				VK_F6,
				VK_F7,
				VK_F8,
				VK_F9,
				VK_G,
				VK_DEAD_TILDE,
				VK_H,
				VK_HOME,
				VK_I,
				VK_INSERT,
				VK_J,
				VK_K,
				VK_L,
				VK_ALT,
				VK_OPEN_BRACKET,
				VK_CONTROL,
				VK_LEFT,
				VK_CONTEXT_MENU,
				VK_SHIFT,
				VK_WINDOWS,
				VK_M,
				VK_MINUS,
				VK_MULTIPLY,
				VK_N,
				VK_NUM_LOCK,
				VK_NUMPAD0,
				VK_NUMPAD1,
				VK_NUMPAD2,
				VK_NUMPAD3,
				VK_NUMPAD4,
				VK_NUMPAD5,
				VK_NUMPAD6,
				VK_NUMPAD7,
				VK_NUMPAD8,
				VK_NUMPAD9,
				VK_COMMA,
				VK_ENTER,
				VK_EQUALS,
				VK_O,
				VK_P,
				VK_PAUSE,
				VK_PERIOD,
				VK_Q,
				VK_R,
				VK_ALT,
				VK_CLOSE_BRACKET,
				VK_CONTROL,
				VK_ENTER,
				VK_RIGHT,
				VK_CONTEXT_MENU,
				VK_SHIFT,
				VK_WINDOWS,
				VK_S,
				VK_SEMICOLON,
				VK_SLASH,
				VK_SPACE,
				VK_SUBTRACT,
				VK_T,
				VK_TAB,
				VK_U,
				VK_UP,
				VK_V,
				VK_W,
				VK_X,
				VK_Y,
				VK_Z
		};
		
		for (int i = 0; i < slick.length; i++) {
			slickToSwing.put(slick[i], swing[i]);
			swingToSlick.put(swing[i], slick[i]);
		}
	}

}