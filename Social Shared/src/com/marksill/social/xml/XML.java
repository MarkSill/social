package com.marksill.social.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
import com.marksill.social.instance.InstanceGame;
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
		return Instance.game;
	}
	
	private void create(Instance parent, Element e) {
		Instance i = null;
		String type = e.getAttributeValue("type");
		switch(type) {
		case "InstanceWorld":
			InstanceWorld world = new InstanceWorld();
			world.gravX = Double.parseDouble(e.getChildText("gravX"));
			world.gravY = Double.parseDouble(e.getChildText("gravY"));
			i = world;
			break;
		case "InstanceBlock": case "InstanceRectangle": case "InstanceCircle":
			InstanceBlock block;
			if (type.equals("InstanceRectangle")) {
				InstanceRectangle rect = new InstanceRectangle();
				Vector2 size = new Vector2();
				size.x = Double.parseDouble(e.getChildText("sizeX"));
				size.y = Double.parseDouble(e.getChildText("sizeY"));
				rect.size = size;
				block = rect;
			} else if (type.equals("InstanceCircle")) {
				InstanceCircle circ = new InstanceCircle();
				circ.radius = Double.parseDouble(e.getChildText("radius"));
				block = circ;
			} else {
				block = new InstanceBlock();
			}
			block.anchored = Boolean.parseBoolean(e.getChildText("anchored"));
			block.visible = Boolean.parseBoolean(e.getChildText("visible"));
			block.mass = Double.parseDouble(e.getChildText("mass"));
			block.density = Double.parseDouble(e.getChildText("density"));
			block.elasticity = Double.parseDouble(e.getChildText("elasticity"));
			block.friction = Double.parseDouble(e.getChildText("friction"));
			Color color = new Color(0, 0, 0);
			color.r = Float.parseFloat(e.getChildText("colorR"));
			color.g = Float.parseFloat(e.getChildText("colorG"));
			color.b = Float.parseFloat(e.getChildText("colorB"));
			color.a = Float.parseFloat(e.getChildText("colorA"));
			block.color = color;
			block.rotationLocked = Boolean.parseBoolean(e.getChildText("rotationLocked"));
			block.rotation = Double.parseDouble(e.getChildText("rotation"));
			i = block;
			break;
		case "InstanceScript": case "InstanceClientScript":
			InstanceScript script;
			if (type.equals("InstanceScript")) {
				script = new InstanceScript();
			} else {
				script = new InstanceClientScript();
			}
			script.code = e.getChildText("code");
			script.enabled = Boolean.parseBoolean(e.getChildText("enabled"));
			i = script;
			break;
		case "InstanceGame":
			InstanceGame game = new InstanceGame();
			i = game;
			break;
		case "InstancePlayers":
			InstancePlayers players = new InstancePlayers();
			players.maxPlayers = Integer.parseInt(e.getChildText("maxPlayers"));
			i = players;
			break;
		case "InstanceContainer":
			InstanceContainer container = new InstanceContainer();
			i = container;
			break;
		case "InstancePlayer": break;
		default:
			i = new Instance();
			break;
		}
		if (i != null) {
			i.name = e.getChildText("name");
			if (parent != null) {
				parent.addChild(i);
			}
		}
		
		List<Element> elements = e.getChildren("instance");
		for (Element element : elements) {
			create(i, element);
		}
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
