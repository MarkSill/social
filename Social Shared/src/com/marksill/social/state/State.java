package com.marksill.social.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.marksill.social.Social;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceCamera;
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
	
	@Override
	public void mousePressed(int button, int x, int y) {
		InstanceCamera cam = InstanceCamera.getCamera();
		float fx = (float) -(((Social.getInstance().getContainer().getWidth() / 2) / NotGameState.PPM - cam.position.x) - (x / NotGameState.PPM));
		float fy = (float) (((Social.getInstance().getContainer().getHeight() / 2) / NotGameState.PPM - cam.position.y) - (y / NotGameState.PPM));
		if (Social.getInstance().isRunning()) {
			super.mousePressed(button, x, y);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireMousePressCallbacks(button, fx, fy);
			}
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		InstanceCamera cam = InstanceCamera.getCamera();
		float fx = (float) -(((Social.getInstance().getContainer().getWidth() / 2) / NotGameState.PPM - cam.position.x) - (x / NotGameState.PPM));
		float fy = (float) (((Social.getInstance().getContainer().getHeight() / 2) / NotGameState.PPM - cam.position.y) - (y / NotGameState.PPM));
		if (Social.getInstance().isRunning()) {
			super.mouseReleased(button, x, y);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireMouseReleaseCallbacks(button, fx, fy);
			}
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		InstanceCamera cam = InstanceCamera.getCamera();
		float fx = (float) -(((Social.getInstance().getContainer().getWidth() / 2) / NotGameState.PPM - cam.position.x) - (x / NotGameState.PPM));
		float fy = (float) (((Social.getInstance().getContainer().getHeight() / 2) / NotGameState.PPM - cam.position.y) - (y / NotGameState.PPM));
		if (Social.getInstance().isRunning()) {
			super.mouseClicked(button, x, y, clickCount);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireMouseClickCallbacks(button, fx, fy, clickCount);
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		InstanceCamera cam = InstanceCamera.getCamera();
		float foldx = (float) -(((Social.getInstance().getContainer().getWidth() / 2) / NotGameState.PPM - cam.position.x) - (oldx / NotGameState.PPM));
		float foldy = (float) (((Social.getInstance().getContainer().getHeight() / 2) / NotGameState.PPM - cam.position.y) - (oldy / NotGameState.PPM));
		float fnewx = (float) -(((Social.getInstance().getContainer().getWidth() / 2) / NotGameState.PPM - cam.position.x) - (newx / NotGameState.PPM));
		float fnewy = (float) (((Social.getInstance().getContainer().getHeight() / 2) / NotGameState.PPM - cam.position.y) - (newy / NotGameState.PPM));
		if (Social.getInstance().isRunning()) {
			super.mouseMoved(oldx, oldy, newx, newy);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireMouseMovedCallbacks(foldx, foldy, fnewx, fnewy);
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(int newValue) {
		if (Social.getInstance().isRunning()) {
			super.mouseWheelMoved(newValue);
			for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
				pl.fireMouseWheelCallbacks(newValue);
			}
			
		}
	}

}
