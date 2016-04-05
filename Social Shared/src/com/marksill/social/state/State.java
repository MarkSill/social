package com.marksill.social.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.marksill.social.Social;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstancePlayer;
import com.marksill.social.instance.InstancePlayers;

/**
 * Class for containing NotStates in a game with Slick running.
 */
public class State extends BasicGameState {
	
	/** The ID of the State. */
	protected int id;
	/** The State's NotState. */
	protected NotState notState;
	
	/**
	 * Creates a new State.
	 * @param id The ID of the state.
	 * @param notState The NotState of the State.
	 */
	public State(int id, NotState notState) {
		this.id = id;
		this.notState = notState;
	}

	@Override
	public void init(GameContainer container, StateBasedGame sbgame) throws SlickException {
		Social game = (Social) sbgame;
		notState.init(game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbgame, Graphics g) throws SlickException {
		Social game = (Social) sbgame;
		notState.render(game, g);
		game.globalRender(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbgame, int delta) throws SlickException {
		Social game = (Social) sbgame;
		notState.update(game, delta);
		game.globalUpdate(container, delta);
	}
	
	/**
	 * Resets the State.
	 * @param container The current container.
	 * @param sbgame The StateBasedGame (instance of Social).
	 */
	public void reset(GameContainer container, StateBasedGame sbgame) {
		Social game = (Social) sbgame;
		notState.reset(game);
	}

	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (Social.getInstance().isRunning()) {
			super.keyPressed(key, c);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireKeyboardDownCallbacks(key, c);
			}
		}
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (Social.getInstance().isRunning()) {
			super.keyReleased(key, c);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireKeyboardUpCallbacks(key, c);
			}
		}
	}

}
