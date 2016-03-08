package com.marksill.social.state;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import com.marksill.social.instance.InstanceWorld;

/**
 * NotState for the game.
 */
public class NotGameState extends NotState {
	
	/** The NotState's ID. */
	public static final int ID = 0;
	/** The Pixels Per Meter ratio. */
	public static final float PPM = 32;
	
	/** The list of instances. */
	private static List<Instance> instances = new ArrayList<Instance>();
	private static List<Instance> toRemove = new ArrayList<Instance>();
	private static List<Instance> toAdd = new ArrayList<Instance>();

	/**
	 * Creates a new NotGameState.
	 */
	public NotGameState() {
		super(ID);
	}

	@Override
	public void init(Social social) {
		Instance.game = new InstanceGame();
	}

	@Override
	public void update(Social social, int delta) {
		if (Instance.game == null || social.getCanvasContainer() != null) {
			try {
				Class<?> editorClass = Class.forName("com.marksill.social.SocialEditor");
				Object editor = new Object();
				editor = editorClass.getField("editor").get(editor);
				Method method = editorClass.getMethod("buildTree", (Class<?>[]) null);
				method.invoke(editor);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
				e.printStackTrace();
			}
			//return;
		}
		Object[] copy = toRemove.toArray();
		toRemove.clear();
		for (int i = 0; i < copy.length; i++) {
			instances.remove((Instance) copy[i]);
		}
		copy = toAdd.toArray();
		toAdd.clear();
		for (int i = 0; i < copy.length; i++) {
			instances.add((Instance) copy[i]);
		}
		for (Instance i : instances) {
			i.update(delta);
		}
	}
	
	@Override
	public void render(Social social, Graphics g) {
		if (Instance.game == null) {
			return;
		}
		renderInstances(Instance.game.findChild("World"), g, social);
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
	
	public static void addInstance(Instance instance) {
		toAdd.add(instance);
	}
	
	public static void removeInstance(Instance instance) {
		toRemove.add(instance);
	}

}
