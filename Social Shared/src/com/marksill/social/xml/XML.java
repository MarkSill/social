package com.marksill.social.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceBlock;
import com.marksill.social.instance.InstanceGame;
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
			document.setRootElement(root);
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
		InstanceGame gamei = new InstanceGame();
		Element game = root.getChild("game");
		create(gamei, game);
		return gamei;
	}
	
	private void create(Instance parent, Element root) {
		List<Element> elements = root.getChildren("instance");
		for (Element e : elements) {
			Instance i = null;
			switch(e.getAttributeValue("type")) {
			case "InstanceWorld":
				InstanceWorld world = new InstanceWorld();
				i = world;
				break;
			case "InstanceBlock":
				InstanceBlock block = new InstanceBlock();
				block.anchored = Boolean.parseBoolean(e.getChildText("anchored"));
				block.visible = Boolean.parseBoolean(e.getChildText("visible"));
				block.mass = Double.parseDouble(e.getChildText("mass"));
				block.density = Double.parseDouble(e.getChildText("density"));
				block.elasticity = Double.parseDouble(e.getChildText("elasticity"));
				i = block;
				break;
			case "InstanceScript":
				InstanceScript script = new InstanceScript();
				script.code = e.getChildText("code");
				script.enabled = Boolean.parseBoolean(e.getChildText("enabled"));
				i = script;
			case "InstanceGame":
				InstanceGame game = new InstanceGame();
				game.maxPlayers = Integer.parseInt(e.getChildText("maxplayers"));
				i = game;
			default:
				i = new Instance();
				break;
			}
			if (i != null) {
				i.name = e.getChildText("name");
				parent.addChild(i);
				create(i, e);
			}
		}
	}
	
	public void saveGame(InstanceGame gamei, String filename) {
		Element game = root.addContent(new Element("game"));
		save(gamei, game);
		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat());
		try {
			output.output(document, new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void save(Instance inst, Element parent) {
		List<Instance> children = inst.getChildren();
		for (Instance i : children) {
			Element e = new Element("instance");
			String className = i.getClass().getSimpleName();
			e.setAttribute("type", className);
			e.addContent(new Element("name").setText(i.name));
			switch (className) {
			case "InstanceBlock":
				InstanceBlock block = (InstanceBlock) i;
				e.addContent(new Element("anchored").setText(String.valueOf(block.anchored)));
				e.addContent(new Element("visible").setText(String.valueOf(block.visible)));
				e.addContent(new Element("mass").setText(String.valueOf(block.mass)));
				e.addContent(new Element("density").setText(String.valueOf(block.density)));
				e.addContent(new Element("elasticity").setText(String.valueOf(block.elasticity)));
				break;
			case "InstanceGame":
				InstanceGame game = (InstanceGame) i;
				e.addContent(new Element("maxplayers").setText(String.valueOf(game.maxPlayers)));
				break;
			case "InstanceScript":
				InstanceScript script = (InstanceScript) i;
				e.addContent(new Element("enabled").setText(String.valueOf(script.enabled)));
				e.addContent(new Element("code").setText(script.code));
				break;
			case "InstanceWorld":
				break;
			default:
				break;
			}
			parent.addContent(e);
		}
	}

}
