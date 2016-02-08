package com.marksill.social.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.marksill.social.Social;

public class State extends BasicGameState {
	
	protected int id;
	protected NotState notState;
	
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
	
	public void reset(GameContainer container, StateBasedGame sbgame) {
		Social game = (Social) sbgame;
		notState.reset(game);
	}

	@Override
	public int getID() {
		return id;
	}

}
