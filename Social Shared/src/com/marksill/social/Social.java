package com.marksill.social;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.marksill.social.instance.Instance;
import com.marksill.social.networking.NetworkInterface;
import com.marksill.social.networking.NetworkServer;
import com.marksill.social.state.NotGameState;
import com.marksill.social.state.NotState;
import com.marksill.social.state.State;

/**
 * The main class for Social. Used by all types of the game (normal, editor, and server).
 */
public class Social extends StateBasedGame {
	
	/** The debug mode of Social. */
	public static boolean debug = true;
	/** The instance of Social. */
	public static Social social;
	
	/** Does this instance have graphics enabled (e.g. is it not a server?)? */
	private boolean graphics = false;
	/** Does this instance use Swing backend? */
	private boolean swing = false;
	/** The CanvasGameContainer if using Swing. */
	private SocialCanvas canvas = null;
	/** The AppGameContainer if not using Swing. */
	private AppGameContainer appgc = null;
	/** The general container for this. */
	private GameContainer generalContainer = null;
	/** The current frame rate. */
	private double fps = 0;
	/** The last timestamp since the FPS was updated. */
	private long lastTime = 0;
	private boolean running;
	private NetworkInterface network = null;
	private SSLSocketFactory sslSocketFactory;
	
	/**
	 * Creates a new instance of Social.
	 */
	public Social() {
		super("Social");
		social = this;
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
		running = true;
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			FileInputStream stream = new FileInputStream("res/cert.crt");
			Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(stream);
			stream.close();
			keyStore.setCertificateEntry("test", cert);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tmf.getTrustManagers(), null);
			sslSocketFactory = ctx.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (graphics) {
			try {
				if (swing) {
					generalContainer = (canvas = new SocialCanvas(this)).getContainer();
				} else {
					generalContainer = appgc = new AppGameContainer(this, 800, 600, false);
				}
				generalContainer.setTargetFrameRate(60);
				generalContainer.setShowFPS(false);
				generalContainer.setAlwaysRender(true);
				Controllers.setup();
				if (!swing) {
					appgc.start();
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new NotGameState());
	}
	
	public void setNetworkInterface(NetworkInterface network) {
		this.network = network;
	}
	
	public NetworkInterface getNetworkInterface() {
		return network;
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
	
	/**
	 * Returns the general container.
	 * @return The instance's container.
	 */
	public GameContainer getGeneralContainer() {
		return generalContainer;
	}
	
	/**
	 * Adds a NotState and creates a State out of it.
	 * @param notState The NotState to add.
	 */
	public void addState(NotState notState) {
		State s = new State(notState.getID(), notState);
		addState(s);
	}
	
	/**
	 * A function that renders things from every State.
	 * @param container The current container.
	 * @param g The current graphics object.
	 */
	public void globalRender(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		if (debug) {
			String debugStr = Math.round(fps) + " FPS";
			g.drawString(debugStr, 0f, 0f);
		}
	}
	
	/**
	 * A function that updates things from every NotState.
	 * @param container The current container.
	 * @param delta The time (in milliseconds) since the last update.
	 */
	public void globalUpdate(GameContainer container, int delta) {
		fps = 1000000000 / (System.nanoTime() - lastTime);
		lastTime = System.nanoTime();
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			if (swing) {
				//((JFrame) canvas.getParent().getParent().getParent().getParent()).dispose(); //A lot of getParents()....
			} else {
				container.exit();
			}
		}
		if (swing) {
			if (!canvas.added) {
				container.getInput().addKeyListener(canvas);
			}
		}
		Controllers.pollControllers();
	}
	
	/**
	 * Gets the instance of Social.
	 * @return The instance.
	 */
	public static Social getInstance() {
		return social;
	}
	
	/**
	 * Stops the game.
	 */
	public void shutdown() {
		if (graphics) {
			getContainer().exit();
		}
	}
	
	public boolean isRunning() {
		return running && Instance.game != null;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isServer() {
		return getNetworkInterface() instanceof NetworkServer;
	}
	
	public boolean isNetworked() {
		return network != null;
	}
	
	public SSLSocketFactory getSSLSocketFactory() {
		return sslSocketFactory;
	}

}
