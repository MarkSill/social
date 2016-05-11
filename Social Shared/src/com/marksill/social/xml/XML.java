package com.marksill.social.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Vector2;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.newdawn.slick.Color;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceBlock;
import com.marksill.social.instance.InstanceCircle;
import com.marksill.social.instance.InstanceClientScript;
import com.marksill.social.instance.InstanceContainer;
import com.marksill.social.instance.InstanceEvent;
import com.marksill.social.instance.InstanceGame;
import com.marksill.social.instance.InstanceImages;
import com.marksill.social.instance.InstanceJoints;
import com.marksill.social.instance.InstancePlayers;
import com.marksill.social.instance.InstanceRectangle;
import com.marksill.social.instance.InstanceScript;
import com.marksill.social.instance.InstanceWorld;

public class XML {
	
	private Document document;
	private Element root;

	public XML() {
		this(null);
	}
	
	public XML(String filename) {
		if (filename == null) {
			root = new Element("root");
			document = new Document(root);
		} else {
			SAXBuilder builder = new SAXBuilder();
			File file = new File(filename);
			try {
				document = builder.build(file);
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
			root = document.getRootElement();
		}
	}
	
	public InstanceGame createGame() {
		Element game = root.getChild("instance");
		create(null, game);
		if (Instance.game.findChild("World") == null) {
			new InstanceWorld(Instance.game);
		}
		if (Instance.game.findChild("Players") == null) {
			new InstancePlayers(Instance.game);
		}
		if (Instance.game.findChild("Images") == null) {
			new InstanceImages(Instance.game);
		}
		return Instance.game;
	}
	
	private void create(Instance parent, Element e) {
		Instance i = null;
		String type = e.getAttributeValue("type");
		switch(type) {
		case "InstanceWorld":
			InstanceWorld world = new InstanceWorld();
			world.gravX = (double) get(e, "gravX", 0.0, Double.class);
			world.gravY = (double) get(e, "gravY", -9.81, Double.class);
			world.speed = (double) get(e, "speed", 1.0, Double.class);
			i = world;
			break;
		case "InstanceBlock": case "InstanceRectangle": case "InstanceCircle":
			InstanceBlock block;
			if (type.equals("InstanceRectangle")) {
				InstanceRectangle rect = new InstanceRectangle();
				Vector2 size = new Vector2();
				size.x = (double) get(e, "sizeX", 1.0, Double.class);
				size.y = (double) get(e, "sizeY", 1.0, Double.class);
				rect.size = size;
				block = rect;
			} else if (type.equals("InstanceCircle")) {
				InstanceCircle circ = new InstanceCircle();
				circ.radius = (double) get(e, "radius", 0.5, Double.class);
				block = circ;
			} else {
				block = new InstanceBlock();
			}
			Vector2 pos = new Vector2();
			pos.x = (double) get(e, "posX", 0.0, Double.class);
			pos.y = (double) get(e, "posY", 0.0, Double.class);
			block.position = pos;
			Vector2 vel = new Vector2();
			vel.x = (double) get(e, "velX", 0.0, Double.class);
			vel.y = (double) get(e, "velY", 0.0, Double.class);
			block.velocity = vel;
			block.anchored = (boolean) get(e, "anchored", false, Boolean.class);
			block.visible = (boolean) get(e, "visible", true, Boolean.class);
			block.mass = (double) get(e, "mass", 1.0, Double.class);
			block.density = (double) get(e, "density", BodyFixture.DEFAULT_DENSITY, Double.class);
			block.elasticity = (double) get(e, "elasticity", BodyFixture.DEFAULT_RESTITUTION, Double.class);
			block.friction = (double) get(e, "friction", BodyFixture.DEFAULT_FRICTION, Double.class);
			Color color = new Color(0, 0, 0);
			color.r = (float) get(e, "colorR", 1f, Float.class);
			color.g = (float) get(e, "colorG", 1f, Float.class);
			color.b = (float) get(e, "colorB", 1f, Float.class);
			color.a = (float) get(e, "colorA", 1f, Float.class);
			block.color = color;
			block.rotationLocked = (boolean) get(e, "rotationLocked", false, Boolean.class);
			block.rotation = (double) get(e, "rotation", 0.0, Double.class);
			block.image = (String) get(e, "image", null, null);
			i = block;
			break;
		case "InstanceScript": case "InstanceClientScript":
			InstanceScript script;
			if (type.equals("InstanceScript")) {
				script = new InstanceScript();
			} else {
				script = new InstanceClientScript();
			}
			script.code = (String) get(e, "code", "", null);
			script.enabled = (boolean) get(e, "enabled", true, Boolean.class);
			i = script;
			break;
		case "InstanceGame":
			InstanceGame game = new InstanceGame();
			i = game;
			break;
		case "InstancePlayers":
			InstancePlayers players = new InstancePlayers();
			players.maxPlayers = (int) get(e, "maxPlayers", 4, Integer.class);
			i = players;
			break;
		case "InstanceContainer":
			InstanceContainer container = new InstanceContainer();
			i = container;
			break;
		case "InstanceJoints":
			InstanceJoints joints = new InstanceJoints();
			i = joints;
		case "InstancePlayer": break;
		case "InstanceEvent":
			InstanceEvent event = new InstanceEvent();
			i = event;
			break;
		case "InstanceImages":
			InstanceImages images = new InstanceImages();
			i = images;
			break;
		default:
			i = new Instance();
			break;
		}
		if (i != null) {
			i.name = (String) get(e, "name", "Instance", null);
			i.id = (Long) get(e, "id", Instance.nextID++, Long.class);
			if (i.id > Instance.nextID) {
				Instance.nextID = i.id + 1;
			}
			if (parent != null) {
				i.setParent(parent);
			}
		}
		
		List<Element> elements = e.getChildren("instance");
		for (Element element : elements) {
			create(i, element);
		}
	}
	
	private Object get(Element e, String name, Object def, Class<?> clazz) {
		String text = e.getChildText(name);
		if (text == null) {
			return def;
		}
		if (clazz != null) {
			if (clazz == Double.class) {
				return Double.parseDouble(text);
			} else if (clazz == Float.class) {
				return Float.parseFloat(text);
			} else if (clazz == Integer.class) {
				return Integer.parseInt(text);
			} else if (clazz == Long.class) {
				return Long.parseLong(text);
			} else if (clazz == Boolean.class) {
				return Boolean.parseBoolean(text);
			}
		}
		return text;
	}
	
	public void saveGame(InstanceGame gamei, String filename) {
		save(gamei, root);
		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat());
		try {
			output.output(document, new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void save(Instance i, Element parent) {
		Element e = new Element("instance");
		String className = i.getClass().getSimpleName();
		e.setAttribute("type", className);
		e.addContent(new Element("name").setText(i.name));
		e.addContent(new Element("id").setText(String.valueOf(i.id)));
		switch (className) {
		case "InstanceBlock": case "InstanceRectangle": case "InstanceCircle":
			InstanceBlock block;
			if (className.equals("InstanceRectangle")) {
				InstanceRectangle rect = (InstanceRectangle) i;
				e.addContent(new Element("sizeX").setText(String.valueOf(rect.size.x)));
				e.addContent(new Element("sizeY").setText(String.valueOf(rect.size.y)));
				block = rect;
			} else if (className.equals("InstanceCircle")) {
				InstanceCircle circ = (InstanceCircle) i;
				e.addContent(new Element("radius").setText(String.valueOf(circ.radius)));
				block = circ;
			} else {
				block = (InstanceBlock) i;
			}
			e.addContent(new Element("posX").setText(String.valueOf(block.position.x)));
			e.addContent(new Element("posY").setText(String.valueOf(block.position.y)));
			e.addContent(new Element("anchored").setText(String.valueOf(block.anchored)));
			e.addContent(new Element("visible").setText(String.valueOf(block.visible)));
			e.addContent(new Element("mass").setText(String.valueOf(block.mass)));
			e.addContent(new Element("density").setText(String.valueOf(block.density)));
			e.addContent(new Element("elasticity").setText(String.valueOf(block.elasticity)));
			e.addContent(new Element("friction").setText(String.valueOf(block.friction)));
			Color color = block.color;
			e.addContent(new Element("colorR").setText(String.valueOf(color.r)));
			e.addContent(new Element("colorG").setText(String.valueOf(color.g)));
			e.addContent(new Element("colorB").setText(String.valueOf(color.b)));
			e.addContent(new Element("colorA").setText(String.valueOf(color.a)));
			e.addContent(new Element("rotationLocked").setText(String.valueOf(block.rotationLocked)));
			e.addContent(new Element("rotation").setText(String.valueOf(block.rotation)));
			e.addContent(new Element("image").setText(block.image));
			break;
		case "InstancePlayers":
			InstancePlayers players = (InstancePlayers) i;
			e.addContent(new Element("maxPlayers").setText(String.valueOf(players.maxPlayers)));
			break;
		case "InstanceScript": case "InstanceClientScript":
			InstanceScript script = (InstanceScript) i;
			e.addContent(new Element("enabled").setText(String.valueOf(script.enabled)));
			e.addContent(new Element("code").setText(script.code));
			break;
		case "InstanceWorld":
			InstanceWorld world = (InstanceWorld) i;
			e.addContent(new Element("gravX").setText(String.valueOf(world.gravX)));
			e.addContent(new Element("gravY").setText(String.valueOf(world.gravY)));
			e.addContent(new Element("speed").setText(String.valueOf(world.speed)));
			break;
		default:
			break;
		}
		parent.addContent(e);
		
		List<Instance> children = i.getChildren();
		for (Instance inst : children) {
			save(inst, e);
		}
	}

}
