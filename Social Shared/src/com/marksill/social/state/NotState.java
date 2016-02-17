package com.marksill.social.state;

import org.newdawn.slick.Graphics;

import com.marksill.social.Social;

/**
 * Base class for NotStates. NotStates are similar to States, except that they don't require Slick to be running to work.
 */
public abstract class NotState {
	
	/** The NotState's ID. */
	private int id;

	/**
	 * Creates a new NotState.
	 * @param id The ID of the NotState.
	 */
	public NotState(int id) {
		this.id = id;
	}
	
	/**
	 * Initializes the NotState.
	 * @param social The instance of Social.
	 */
	public abstract void init(Social social);
	/**
	 * Updates the NotState.
	 * @param social The instance of Social.
	 * @param delta The time since the last update in milliseconds.
	 */
	public abstract void update(Social social, int delta);
	/**
	 * Renders the NotState. Should only be called if Slick is running.
	 * @param social The instance of Social.
	 * @param g The current graphics object.
	 */
	public abstract void render(Social social, Graphics g);
	/**
	 * Resets the NotState.
	 * @param social The instance of Social.
	 */
	public abstract void reset(Social social);
	
	/**
	 * Gets the NotState's ID.
	 * @return The NotState's ID.
	 */
	public int getID() {
		return id;
	}

}
