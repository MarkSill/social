package com.marksill.social.state;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

import com.marksill.social.Social;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceBlock;
import com.marksill.social.instance.InstanceGame;
import com.marksill.social.instance.InstanceScript;
import com.marksill.social.instance.InstanceWorld;

/**
 * NotState for the game.
 */
public class NotGameState extends NotState {
	
	/** The NotState's ID. */
	public static final int ID = 0;
	/** The Pixels Per Meter ratio. */
	public static final float PPM = 32;
	
	public static boolean running = true;
	
	/** The list of instances. */
	private static List<Instance> toRemove = new ArrayList<Instance>();
	private static List<Instance> toAdd = new ArrayList<Instance>();
	private static boolean lastRunning = !running;
	private static InstanceGame startGame;
	
	int n = 0;

	/**
	 * Creates a new NotGameState.
	 */
	public NotGameState() {
		super(ID);
	}

	@Override
	public void init(Social social) {
		Instance.game = new InstanceGame();
		startGame = Instance.game.clone();
	}

	@Override
	public void update(Social social, int delta) {
		if (n++ >= 100) {
			running = true;
			if (n >= 200) {
				running = false;
				n = 0;
			}
		}
		//FIXME: Figure out why blocks aren't being cloned.
		if (lastRunning != running) {
			if (running) {
				startGame = Instance.game.clone();
			} else {
				killScripts(Instance.game);
				startGame.clone(true);
			}
		}
		lastRunning = running;
		if (running) {
			updateInstances(Instance.game, delta, social);
		}
	}
	
	@Override
	public void render(Social social, Graphics g) {
		renderInstances(Instance.game.findChild("World"), g, social);
		g.drawString("Running: " + String.valueOf(running), 0, 20);
	}

	@Override
	public void reset(Social social) {
		
	}
	
	/**
	 * Renders all instances (that need to be rendered).
	 * @param parent The parent to check.
	 * @param g The current graphics object.
	 * @param social The instance of Social.
	 */
	private void renderInstances(Instance parent, Graphics g, Social social) {
		g.setAntiAlias(true);
		if (parent == null) {
			return;
		}
		if (parent instanceof InstanceBlock) {
			InstanceBlock block = (InstanceBlock) parent;
			if (block.visible) {
				Body body = block.getBody();
				if (((InstanceWorld) Instance.game.findChild("World")).getWorld().containsBody(body)) {
					Vector2 pos = body.getWorldCenter();//.difference(body.getLocalCenter());
					List<BodyFixture> fixtures = body.getFixtures();
					float rot = (float) (-Math.toDegrees(body.getTransform().getRotation()));
					g.pushTransform();
					g.translate((float) pos.x * PPM, (float) -pos.y * PPM + social.getContainer().getHeight());
					g.rotate(0, 0, rot);
					for (BodyFixture f : fixtures) {
						Convex shape = f.getShape();
						g.setColor(block.color);
						g.pushTransform();
						g.translate((float) shape.getCenter().x * PPM, (float) shape.getCenter().y * PPM);
						if (shape instanceof Rectangle) {
							Rectangle rect = (Rectangle) shape;
							float srot = (float) (-Math.toDegrees(rect.getRotation()));
							g.pushTransform();
							g.rotate(0, 0, srot);
							float w = (float) (rect.getWidth()) * PPM;
							float h = (float) (rect.getHeight()) * PPM;
							g.fillRect(-w / 2, -h / 2, w, h);
							g.popTransform();
						} else if (shape instanceof Circle) {
							Circle cir = (Circle) shape;
							float rad = (float) (cir.getRadius() * 2) * PPM;
							g.fillOval(-rad / 2, -rad / 2, rad, rad);
						}
						g.popTransform();
					}
					g.popTransform();
				} else {
					block.delete();
				}
			}
		}
		List<Instance> children = parent.getChildren();
		for (int i = 0; i < children.size(); i++) {
			renderInstances(children.get(i), g, social);
		}
	}
	
	private void updateInstances(Instance parent, int delta, Social social) {
		parent.update(delta);
		for (Instance i : parent.getChildren()) {
			updateInstances(i, delta, social);
		}
	}
	
	public static void addInstance(Instance instance) {
		toAdd.add(instance);
	}
	
	public static void removeInstance(Instance instance) {
		toRemove.add(instance);
	}
	
	private static void killScripts(Instance parent) {
		if (parent instanceof InstanceScript) {
			InstanceScript script = (InstanceScript) parent;
			if (script.thread != null) {
				script.thread.kill();
			}
		}
		for (Instance i : parent.getChildren()) {
			killScripts(i);
		}
	}

}
