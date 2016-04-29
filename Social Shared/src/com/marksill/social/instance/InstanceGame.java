package com.marksill.social.instance;

import java.util.Map;

import com.marksill.social.Social;

/**
 * Container instance meant for containing the entire game.
 */
public class InstanceGame extends Instance {
	
	/** The game's class name. */
	public static final String CLASS_NAME = "Game";

	/**
	 * Creates a new game.
	 */
	public InstanceGame() {
		super(CLASS_NAME);
	}

	/**
	 * Creates a new game.
	 * @param name The name of the game.
	 */
	public InstanceGame(String name) {
		super(name);
	}

	/**
	 * Creates a new game.
	 * @param parent The parent of the game.
	 */
	public InstanceGame(Instance parent) {
		super(CLASS_NAME, parent);
	}

	/**
	 * Creates a new game.
	 * @param name The name of the game.
	 * @param parent The parent of the game.
	 */
	public InstanceGame(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		Instance.game = this;
	}
	
	/**
	 * Shuts down the game.
	 */
	public void shutdown() {
		Social.social.shutdown();
	}
	
	/**
	 * Shuts down the game after the given amount of time (in milliseconds).
	 * @param time The amount of time to wait before shutting down.
	 */
	public void shutdown(long time) {
		new Thread() {
			@Override
			public void run() {
				try {
					wait(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				shutdown();
			}
		}.start();
	}
	
	public void loadFromMap(Map<String, Object> map) {
		super.loadFromMap(map);
		Instance.game = this;
	}

}
