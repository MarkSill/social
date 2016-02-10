package com.marksill.social;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.marksill.social.state.NotGameState;
import com.marksill.social.state.NotState;
import com.marksill.social.state.State;

public class Social extends StateBasedGame {
	
	public static boolean debug = true;
	/** The instance of Social. */
	public static Social instance;
	
	/** Does this instance have graphics enabled (e.g. is it not a server?)? */
	private boolean graphics = false;
	/** Does this instance use Swing backend? */
	private boolean swing = false;
	/** The CanvasGameContainer if using Swing. */
	private CanvasGameContainer canvas = null;
	/** The AppGameContainer if not using Swing. */
	private AppGameContainer appgc = null;
	private double fps = 0;
	private long lastTime = 0;
	
	/**
	 * Creates a new instance of Social.
	 */
	public Social() {
		super("Social");
		instance = this;
	}
	
	/**
	 * Starts Social with the given parameters.
	 * @param graphics Does the instance have graphics?
	 * @param swing Does the instance use Swing?
	 * @param args The command line arguments.
	 */
	public void start(boolean graphics, boolean swing, String[] args) throws SocialException {
		this.graphics = graphics;
		this.swing = swing;
		if (graphics) {
			try {
				GameContainer container;
				if (swing) {
					container = (canvas = new CanvasGameContainer(this)).getContainer();
				} else {
					container = appgc = new AppGameContainer(this, 800, 600, false);
				}
				container.setTargetFrameRate(60);
				container.setShowFPS(false);
				container.setForceExit(false);
				if (swing) {
					//TODO: Implement canvas rendering.
					throw new SocialException("Canvas rendering is not enabled yet.");
				} else {
					appgc.start();
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
		} else {
			if (swing) {
				
			} else {
				
			}
			//TODO: Implement server-based code.
			throw new SocialException("Server games are not enabled yet.");
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new NotGameState());
	}
	
	/**
	 * Returns true if graphics are enabled.
	 * @return The status of graphics.
	 */
	public boolean graphicsEnabled() {
		return graphics;
	}
	
	/**
	 * Returns true if the instance is using Swing.
	 * @return The status of Swing.
	 */
	public boolean usingSwing() {
		return swing;
	}
	
	/**
	 * Returns the canvas, or null.
	 * @return The instance's canvas.
	 */
	public CanvasGameContainer getCanvasContainer() {
		return canvas;
	}
	
	/**
	 * Returns the app, or null.
	 * @return The instance's app.
	 */
	public AppGameContainer getAppContainer() {
		return appgc;
	}
	
	public void addState(NotState notState) {
		State s = new State(notState.getID(), notState);
		addState(s);
	}
	
	public void globalRender(GameContainer container, Graphics g) throws SlickException {
		g.setColor(Color.white);
		if (debug) {
			String debugStr = Math.round(fps) + " FPS";
			g.drawString(debugStr, 0f, 0f);
		}
	}
	
	public void globalUpdate(GameContainer container, int delta) throws SlickException {
		fps = 1000000000 / (System.nanoTime() - lastTime);
		lastTime = System.nanoTime();
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			container.exit();
		}
	}
	
	/**
	 * Gets the instance of Social.
	 * @return The instance.
	 */
	public static Social getInstance() {
		return instance;
	}
	
	public void shutdown() {
		if (graphics) {
			getContainer().exit();
		}
	}

}
