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

public class NotGameState extends NotState {
	
	public static final int ID = 0;
	public static final float PPM = 32; //Pixels Per Meter
	
	public static List<Instance> instances = new ArrayList<Instance>();
	
	private InstanceGame game;

	public NotGameState() {
		super(ID);
	}

	@Override
	public void init(Social social) {
		game = new InstanceGame();
	}

	@Override
	public void update(Social social, int delta) {
		for (int i = 0; i < instances.size(); i++) {
			instances.get(i).update(delta);
		}
	}
	
	@Override
	public void render(Social social, Graphics g) {
		renderInstances(game.findChild("World"), g, social);
	}

	@Override
	public void reset(Social social) {
		
	}
	
	private void renderInstances(Instance parent, Graphics g, Social social) {
		g.setAntiAlias(true);
		if (parent == null) {
			return;
		}
		if (parent instanceof InstanceBlock) {
			InstanceBlock block = (InstanceBlock) parent;
			if (block.visible) {
				Body body = block.getBody();
				Vector2 pos = body.getWorldCenter().difference(body.getLocalCenter());
				List<BodyFixture> fixtures = body.getFixtures();
				//TODO: Find out why positioning is incorrect depending on rotation with multiple fixtures.
				for (BodyFixture f : fixtures) {
					Convex shape = f.getShape();
					float vx = (float) (pos.x) * PPM;
					float vy = (float) (-pos.y) * PPM + social.getContainer().getHeight();
					float svx = (float) (pos.x + shape.getCenter().x) * PPM;
					float svy = (float) (-pos.y - shape.getCenter().y) * PPM + social.getContainer().getHeight();
					float rot = (float) (-Math.toDegrees(body.getTransform().getRotation()));
					g.rotate(vx, vy, rot);
					g.setColor(block.color);
					if (shape instanceof Rectangle) {
						Rectangle rect = (Rectangle) shape;
						g.rotate(svx, svy, (float) (-Math.toDegrees(rect.getRotation())));
						float w = (float) (rect.getWidth()) * PPM;
						float h = (float) (rect.getHeight()) * PPM;
						g.fillRect(svx - w / 2, svy - h / 2, w, h);
					} else if (shape instanceof Circle) {
						Circle cir = (Circle) shape;
						float rad = (float) (cir.getRadius() * 2) * PPM;
						g.fillOval(svx - rad / 2, svy - rad / 2, rad, rad);
					}
					g.rotate(vx, vy, -rot);
				}
			}
		}
		List<Instance> children = parent.getChildren();
		for (int i = 0; i < children.size(); i++) 
			renderInstances(children.get(i), g, social);
	}

}
