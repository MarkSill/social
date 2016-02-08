package com.marksill.social.state;

import org.newdawn.slick.Graphics;

import com.marksill.social.Social;

public abstract class NotState {
	
	private int id;

	public NotState(int id) {
		this.id = id;
	}
	
	public abstract void init(Social social);
	public abstract void update(Social social, int delta);
	public abstract void render(Social social, Graphics g);
	public abstract void reset(Social social);
	public int getID() {
		return id;
	}

}
