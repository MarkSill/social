package com.marksill.social;

import java.util.ArrayList;
import java.util.List;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstancePlayer;
import com.marksill.social.instance.InstancePlayers;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class Controllers {
	
	public static final String
	LEFT_X = "x",
	LEFT_Y = "y",
	RIGHT_X = "rx",
	RIGHT_Y = "ry",
	Z_AXIS = "z", //TODO: Find some way to make this work with both triggers individually.
	A = "0",
	B = "1",
	X = "2",
	Y = "3",
	LB = "4",
	RB = "5",
	BACK = "6",
	START = "7",
	LEFT_STICK = "8",
	RIGHT_STICK = "9",
	DPAD = "pov"
	;
	
	public static float DEADZONE = 0.1f;
	
	private static List<Controller> controllers = new ArrayList<Controller>();
	
	public static void setup() {
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller c : ca) {
			if (c.getType() == Type.GAMEPAD) {
				controllers.add(c);
			}
		}
	}
	
	public static List<Controller> getControllers() {
		return controllers;
	}
	
	public static void pollControllers() {
		InstancePlayers players = ((InstancePlayers) Instance.game.findChild("Players"));
		List<Controller> controllers = getControllers();
		for (Controller c : controllers) {
			c.poll();
			EventQueue queue = c.getEventQueue();
			Event event = new Event();
			while (queue.getNextEvent(event)) {
				Object value = event.getComponent().getPollData();
				String name = event.getComponent().getIdentifier().toString();
				switch (event.getComponent().getIdentifier().toString()) {
				case LEFT_Y: case RIGHT_Y:
					value = -((float) value);
				case LEFT_X: case RIGHT_X:
					float v = (float) value;
					if ((v > 0 && v < DEADZONE) || (v < 0 && v > -DEADZONE)) {
						value = 0;
					}
					break;
				}
				for (InstancePlayer player : players.getPlayersAsList()) {
					player.fireControllerCallbacks(c, name, value);
				}
			}
		}
	}

}
