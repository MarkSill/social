package com.marksill.social.instance;

import com.marksill.social.Social;

public class InstanceGame extends Instance {
	
	public static final String CLASS_NAME = "Game";
	
	private int maxPlayers = 4;

	public InstanceGame() {
		super(CLASS_NAME);
	}

	public InstanceGame(String name) {
		super(name);
	}

	public InstanceGame(Instance parent) {
		super(CLASS_NAME, parent);
	}

	public InstanceGame(String name, Instance parent) {
		super(name, parent);
	}
	
	@Override
	public void init() {
		Instance.game = this;
		new InstanceWorld(this);
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public void shutdown() {
		Social.instance.shutdown();
	}
	
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

}
