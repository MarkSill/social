package com.marksill.social.state;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.RopeJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.marksill.social.Social;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceBlock;
import com.marksill.social.instance.InstanceCamera;
import com.marksill.social.instance.InstanceCircle;
import com.marksill.social.instance.InstanceClientScript;
import com.marksill.social.instance.InstanceEvent;
import com.marksill.social.instance.InstanceImages;
import com.marksill.social.instance.InstanceJoints;
import com.marksill.social.instance.InstancePlayer;
import com.marksill.social.instance.InstancePlayers;
import com.marksill.social.instance.InstanceRectangle;
import com.marksill.social.instance.InstanceScript;
import com.marksill.social.networking.NetworkServer;

/**
 * NotState for the game.
 */
public class NotGameState extends NotState {
	
	/** The NotState's ID. */
	public static final int ID = 0;
	/** The Pixels Per Meter ratio. */
	public static final float PPM = 32;
	public static final int SELECTION_SIZE = 6;
	
	/** The list of instances. */
	public static List<Instance> instances = new ArrayList<Instance>();
	private static List<Instance> toRemove = new ArrayList<Instance>();
	private static List<Instance> toAdd = new ArrayList<Instance>();
	private static int transparency = 255;
	private static boolean transparencyDirection = false;
	
	/**
	 * Creates a new NotGameState.
	 */
	public NotGameState() {
		super(ID);
	}

	@Override
	public void init(Social social) {
		
	}

	@Override
	public void update(Social social, int delta) {
		if (social.getCanvasContainer() != null && social.graphicsEnabled() && Instance.game != null) {
			try {
				Class<?> editorClass = Class.forName("com.marksill.social.SocialEditor");
				Object editor = new Object();
				editor = editorClass.getField("editor").get(editor);
				Method method = editorClass.getMethod("buildTree", (Class<?>[]) null);
				method.invoke(editor);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
				//e.printStackTrace();
			}
		} else if (Instance.game == null) {
			instances.clear();
			return;
		}
		if (social.isNetworked() && social.isServer()) {
			((NetworkServer) social.getNetworkInterface()).sendUpdate();
		}
		for (Instance i : instances) {
			i.updateVars();
		}
		if (Instance.game == null || !social.isRunning()) {
			if (Instance.game != null) {
				InstanceJoints.removeAllJoints();
			}
			for (Instance i : instances) {
				if (i instanceof InstanceScript && !(i instanceof InstanceClientScript) && ((InstanceScript) i).thread != null) {
					((InstanceScript) i).thread.kill();
					((InstanceScript) i).running = false;
				} else if (i instanceof InstancePlayer) {
					((InstancePlayer) i).clearCallbacks();
				} else if (i instanceof InstanceEvent) {
					((InstanceEvent) i).clearCallbacks();
				} else if (i instanceof InstancePlayers) {
					((InstancePlayers) i).clearCallbacks();
				}
			}
			return;
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
		if (social.isNetworked() && !social.isServer()) {
			return;
		}
		for (Instance i : instances) {
			i.update(delta);
		}
	}
	
	@Override
	public void render(Social social, Graphics g) {
		g.setAntiAlias(true);
		if (Instance.game == null) {
			return;
		}
		
		InstanceCamera cam = InstanceCamera.getCamera();
		g.pushTransform();
		if (cam != null) {
			cam.zoom(g);
			cam.translate(g);
			renderSelection(Instance.game.findChild("World"), g, social, cam);
			renderInstances(Instance.game.findChild("World"), g, social, cam);
			renderJoints(g, social);
		}
		g.popTransform();
		
		if (transparencyDirection) {
			transparency += 15;
		} else {
			transparency -= 15;
		}
		if (transparency >= 255 || transparency <= 70) {
			transparencyDirection = !transparencyDirection;
		}
	}

	@Override
	public void reset(Social social) {
		instances = new ArrayList<>();
		toRemove = new ArrayList<>();
		toAdd = new ArrayList<>();
		transparency = 255;
		transparencyDirection = false;
	}
	
	/**
	 * Renders all instances (that need to be rendered).
	 * @param parent The parent to check.
	 * @param g The current graphics object.
	 * @param social The instance of Social.
	 */
	private void renderInstances(Instance parent, Graphics g, Social social, InstanceCamera cam) {
		if (parent == null) {
			return;
		}
		if (parent instanceof InstanceBlock) {
			InstanceBlock block = (InstanceBlock) parent;
			if (block.visible && canSee(block, cam)) {
				if (block.childOf(Instance.game.findChild("World"))) {
					Vector2 pos = block.position;
					float rot = (float) (-Math.toDegrees(block.rotation));
					g.pushTransform();
					g.translate((float) pos.x * PPM, (float) -pos.y * PPM + social.getContainer().getHeight());
					g.rotate(0, 0, rot);
					g.setColor(block.color);
					if (block instanceof InstanceRectangle) {
						InstanceRectangle rect = (InstanceRectangle) block;
						Vector2 size = rect.size;
						if (size != null) {
							float w = (float) (size.x * PPM), h = (float) (size.y * PPM);
							g.fillRect(-w / 2, -h / 2, w, h);
							if (block.image != null) {
								Image img = InstanceImages.getImage(block.image);
								if (img != null) {
									img.draw(-w / 2, -h / 2, w, h, block.color);
								}
							}
						}
					} else if (block instanceof InstanceCircle) {
						InstanceCircle circ = (InstanceCircle) block;
						float rad = (float) (circ.radius * PPM * 2);
						g.fillOval(-rad / 2, -rad / 2, rad, rad);
					}
					g.popTransform();
				} else {
					block.delete();
				}
			}
		}
		List<Instance> children = parent.getChildren();
		for (int i = 0; i < children.size(); i++) {
			renderInstances(children.get(i), g, social, cam);
		}
	}
	
	private void renderSelection(Instance parent, Graphics g, Social social, InstanceCamera cam) {
		if (parent == null) {
			return;
		}
		g.setLineWidth(SELECTION_SIZE+1);
		if (parent instanceof InstanceBlock) {
			InstanceBlock block = (InstanceBlock) parent;
			if (block.visible && canSee(block, cam) && Instance.selected.contains(block)) {
				if (block.childOf(Instance.game.findChild("World"))) {
					Vector2 pos = block.position;
					float rot = (float) (-Math.toDegrees(block.rotation));
					g.pushTransform();
					g.translate((float) pos.x * PPM, (float) -pos.y * PPM + social.getContainer().getHeight());
					g.rotate(0, 0, rot);
					g.setColor(new Color(0, 128, 255, transparency));
					if (block instanceof InstanceRectangle) {
						InstanceRectangle rect = (InstanceRectangle) block;
						Vector2 size = rect.size;
						float w = (float) (size.x * PPM) + SELECTION_SIZE, h = (float) (size.y * PPM) + SELECTION_SIZE;
						g.fillRect(-w / 2, -h / 2, w, h);
					} else if (block instanceof InstanceCircle) {
						InstanceCircle circ = (InstanceCircle) block;
						float rad = (float) (circ.radius * PPM * 2) + SELECTION_SIZE;
						g.fillOval(-rad / 2, -rad / 2, rad, rad);
					}
					g.popTransform();
				} else {
					block.delete();
				}
			}
		}
		List<Instance> children = parent.getChildren();
		for (int i = 0; i < children.size(); i++) {
			renderSelection(children.get(i), g, social, cam);
		}
	}
	
	public static void renderJoints(Graphics g, Social social) {
		List<Joint> joints = InstanceJoints.getJointsAsList();
		for (Joint j : joints) {
			if (j instanceof DistanceJoint || j instanceof RopeJoint) {
				Vector2 a1 = j.getAnchor1();
				Vector2 a2 = j.getAnchor2();
				g.setLineWidth(1);
				if (j instanceof DistanceJoint) {
					g.setColor(Color.red);
				} else if (j instanceof RopeJoint) {
					g.setColor(Color.orange);
				}
				float x1 = (float) a1.x * PPM, x2 = (float) a2.x * PPM, y1 = (float) (social.getContainer().getHeight() - a1.y * PPM), y2 = (float) (social.getContainer().getHeight() - a2.y * PPM);
				g.drawLine(x1, y1, x2, y2);
			} else if (j instanceof RevoluteJoint || j instanceof WeldJoint) {
				Vector2 pos = j.getAnchor1();
				float x = (float) (pos.x * PPM - 2.5), y = (float) (social.getContainer().getHeight() - pos.y * PPM - 2.5), w = 5, h = 5;
				if (j instanceof RevoluteJoint) {
					g.setColor(Color.blue);
				} else if (j instanceof WeldJoint) {
					g.setColor(Color.yellow);
				}
				g.fillRect(x, y, w, h);
			}
		}
	}
	
	public static void addInstance(Instance instance) {
		toAdd.add(instance);
	}
	
	public static void removeInstance(Instance instance) {
		toRemove.add(instance);
	}
	
	public static boolean canSee(InstanceBlock block, InstanceCamera cam) {
		return true;
		//FIXME: Make this work again.
		/*
		double w = Social.getInstance().getContainer().getWidth() / PPM;
		double h = Social.getInstance().getContainer().getHeight() / PPM;
		Rectangle window = new Rectangle(w, h);
		window.translate(w / 2 + (float) (-cam.position.x), h / 2 + (float) (-cam.position.y));
		AABB aabb = window.createAABB();
		if (block.getBody() != null) {
			Transform transf = block.getBody().getTransform();
			for (BodyFixture f : block.getBody().getFixtures()) {
				if (aabb.overlaps(f.getShape().createAABB(transf))) {
					return true;
				}
			}
		}
		return false;*/
	}
	
	public static void clear() {
		instances.clear();
		instances = new ArrayList<>();
	}

}
