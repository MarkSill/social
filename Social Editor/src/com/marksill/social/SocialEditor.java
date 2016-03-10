package com.marksill.social;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceGame;

public class SocialEditor extends JFrame implements ActionListener, KeyListener, TreeSelectionListener, TreeModelListener, CellEditorListener {

	private static final long serialVersionUID = 2541131438666062756L;
	
	public static SocialEditor editor;
	
	private JTree tree;
	private SocialTreeNode rootNode;
	private JPanel properties;
	private Map<Instance, SocialTreeNode> map, lastMap;
	
	/**
	 * @param args
	 */
	public SocialEditor(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		Social social = new Social();
		try {
			social.start(true, true, args);
		} catch (SocialException e) {
			e.printStackTrace();
		}
		
		//Instance tree:
		rootNode = new SocialTreeNode("game");
		tree = new JTree(rootNode);
		tree.setRootVisible(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.setEditable(true);
		tree.setCellRenderer(new SocialTreeRenderer());
		tree.getCellEditor().addCellEditorListener(this);
		tree.addTreeSelectionListener(this);
		JScrollPane treePane = new JScrollPane(tree);
		treePane.setLayout(new ScrollPaneLayout());
		treePane.setWheelScrollingEnabled(true);
		
		//Properties:
		properties = new JPanel(new BorderLayout());
		properties.setBackground(Color.white);
		
		//Scroll panes for instances and properties
		JScrollPane propertiesPane = new JScrollPane(properties);
		propertiesPane.setLayout(new ScrollPaneLayout());
		
		//Split pane that separates the instances from properties
		JSplitPane browser = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePane, propertiesPane);
		
		//Change some settings for the GameContainer
		CanvasGameContainer container = social.getCanvasContainer();
		container.setSize(800, 600);
		container.getContainer().setUpdateOnlyWhenVisible(false);
		
		//Add the game to a panel
		JPanel gamePane = new JPanel(new BorderLayout());
		gamePane.add(container, BorderLayout.CENTER);
		gamePane.setMinimumSize(new Dimension(10, 10));
		
		//Split pane that separates the game from instances and properties
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, browser, gamePane);
		pane.setOneTouchExpandable(false);
		pane.setDividerLocation(200);
		add(pane);
		
		//Toolbar
		final JMenuBar menubar = new JMenuBar();
		JMenu menu = createMenu(menubar, "File", new MenuItem[] {
				new MenuItem("New", KeyEvent.VK_N, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Open...", KeyEvent.VK_O, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Close", KeyEvent.VK_C, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("{separator}"),
				new MenuItem("Save", KeyEvent.VK_S, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Save As...", KeyEvent.VK_A, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)),
				new MenuItem("{separator}"),
				new MenuItem("Exit", KeyEvent.VK_X, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK))
		});
		menu.setMnemonic(KeyEvent.VK_F);
		menu = createMenu(menubar, "Edit", new MenuItem[] {
				new MenuItem("Undo", KeyEvent.VK_U, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Redo", KeyEvent.VK_R, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)),
				new MenuItem("{separator}"),
				new MenuItem("Cut", KeyEvent.VK_T, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Copy", KeyEvent.VK_C, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Paste", KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK)),
				new MenuItem("Delete", KeyEvent.VK_D, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
		});
		menu.setMnemonic(KeyEvent.VK_E);
		menu = createMenu(menubar, "Insert", new MenuItem[] {
				new MenuItem("Instance", KeyEvent.VK_I),
				new MenuItem("Block", KeyEvent.VK_B),
				new MenuItem("Game", KeyEvent.VK_G),
				new MenuItem("Script", KeyEvent.VK_S),
				new MenuItem("World", KeyEvent.VK_W)
		});
		menu.setMnemonic(KeyEvent.VK_I);
		menu = createMenu(menubar, "Test", new MenuItem[] {
				new MenuItem("Play/Pause", KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0))
		});
		menu.setMnemonic(KeyEvent.VK_T);
		add(menubar, BorderLayout.PAGE_START);
		pack();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Social Editor");
		setLocationRelativeTo(null);
		addKeyListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				social.getCanvasContainer().dispose();
				dispose();
			}
			
			@Override
			public void windowClosed(WindowEvent event) {
				System.exit(0);
			}
		});
		setVisible(true);
		//setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		browser.setDividerLocation(getHeight() / 2);
		
		boolean shouldStart = true;
		try {
			if (shouldStart) {
				social.getCanvasContainer().start();
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		while (Instance.game == null);
		map = new HashMap<Instance, SocialTreeNode>();
		rootNode.setInstance(Instance.game);
		buildTree();
	}

	public static void main(String[] args) {
		editor = new SocialEditor(args);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String inst = null;
		switch (cmd) {
		case "New":
			if (Instance.game != null) {
				actionPerformed(new ActionEvent(this, 0, "Close"));
			}
			Instance.game = new InstanceGame();
			break;
		case "Open...":
			break;
		case "Close":
			if (Instance.game != null) {
				Instance.game.delete();
				Instance.game = null;
			}
			break;
		case "Save":
			break;
		case "Save As...":
			break;
		case "Exit":
			dispose();
			break;
		case "Undo":
			break;
		case "Redo":
			break;
		case "Cut":
			break;
		case "Copy":
			break;
		case "Paste":
			break;
		case "Delete":
			TreePath[] paths = tree.getSelectionPaths();
			if (paths != null) {
				for (TreePath path : paths) {
					SocialTreeNode node = (SocialTreeNode) path.getLastPathComponent();
					if (node != null && node.getInstance() != null) {
						node.getInstance().delete();
					}
				}
			}
			break;
		case "Play/Pause":
			Social.social.setRunning(!Social.social.isRunning());
			break;
			
			//Instances:
		case "Instance":
			inst = "instance";
			break;
		case "Block":
			inst = "block";
			break;
		case "Game":
			inst = "game";
			break;
		case "Script":
			inst = "script";
			break;
		case "World":
			inst = "world";
			break;
		}
		if (inst != null) {
			SocialTreeNode node = (SocialTreeNode) tree.getSelectionPath().getLastPathComponent();
			Instance.create(inst, node.getInstance());
		}
	}
	
	private JMenu createMenu(JMenuBar menubar, String name, MenuItem[] items) {
		final JMenu menu = new JMenu(name);
		for (MenuItem itm : items) {
			final JMenuItem item;
			if (itm.name.equals("{separator}")) {
				menu.addSeparator();
				continue;
			} else {
				item = new JMenuItem(itm.name, itm.key);
				item.setAccelerator(itm.stroke);
				item.addActionListener(this);
			}
			menu.add(item);
		}
		menubar.add(menu);
		return menu;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		ActionEvent event = null;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_N:
			if (e.isControlDown()) {
				event = new ActionEvent(this, 0, "New");
			}
			break;
		case KeyEvent.VK_O:
			if (e.isControlDown()) {
				event = new ActionEvent(this, 0, "Open...");
			}
			break;
		case KeyEvent.VK_S:
			if (e.isControlDown()) {
				if (e.isShiftDown()) {
					event = new ActionEvent(this, 0, "Save as...");
				} else {
					event = new ActionEvent(this, 0, "Save");
				}
			}
			break;
		case KeyEvent.VK_W:
			if (e.isControlDown()) {
				event = new ActionEvent(this, 0, "Close");
			}
			break;
		case KeyEvent.VK_C:
			if (e.isControlDown()) {
				event = new ActionEvent(this, 0, "Copy");
			}
			break;
		case KeyEvent.VK_X:
			if (e.isControlDown()) {
				event = new ActionEvent(this, 0, "Cut");
			}
			break;
		case KeyEvent.VK_V:
			if (e.isControlDown()) {
				event = new ActionEvent(this, 0, "Paste");
			}
			break;
		case KeyEvent.VK_Z:
			if (e.isControlDown()) {
				if (e.isShiftDown()) {
					event = new ActionEvent(this, 0, "Redo");
				} else {
					event = new ActionEvent(this, 0, "Undo");
				}
			}
			break;
		case KeyEvent.VK_F5:
			event = new ActionEvent(this, 0, "Play/Pause");
			break;
		case KeyEvent.VK_DELETE:
			event = new ActionEvent(this, 0, "Delete");
			break;
		}
		if (event != null) {
			actionPerformed(event);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void dispatchAnEvent(AWTEvent e) {
		processEvent(e);
	}
	
	public void buildTree() {
		lastMap = map;
		map = new HashMap<Instance, SocialTreeNode>();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		if (Instance.game == null) {
			tree.setModel(new DefaultTreeModel(null));
			rootNode = null;
			return;
		} else if (rootNode == null) {
			rootNode = new SocialTreeNode("game", Instance.game);
			tree.setModel(new DefaultTreeModel(rootNode));
		}
		rootNode.setUserObject(Instance.game.name);
		buildTree(Instance.game, rootNode, model);
		for (Instance i : lastMap.keySet()) {
			if (map.get(i) == null) {
				model.removeNodeFromParent(lastMap.get(i));
			}
		}
	}
	
	private void buildTree(Instance parent, SocialTreeNode node, DefaultTreeModel model) {
		for (Instance i : new ArrayList<Instance>(parent.getChildren())) {
			SocialTreeNode newNode = (SocialTreeNode) i.node;
			if (newNode == null) {
				newNode = new SocialTreeNode(i.name, i);
				i.node = newNode;
				model.insertNodeInto(newNode, node, 0);
			}
			map.put(i, newNode);
			if (!i.name.equals(newNode.getUserObject())) {
				newNode.setUserObject(i.name);
				model.nodeChanged(newNode);
			}
			buildTree(i, newNode, model);
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		SocialTreeNode node = (SocialTreeNode) e.getPath().getLastPathComponent();
		if (node.getInstance() != null) {
			String className = node.getInstance().getClass().getSimpleName();
			
			switch (className) {
			
			}
		}
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		TreeCellEditor edit = (TreeCellEditor) e.getSource();
		SocialTreeNode node = (SocialTreeNode) tree.getSelectionPath().getLastPathComponent();
		if (node.getInstance() != null) {
			node.getInstance().name = (String) edit.getCellEditorValue();
		}
	}

}

class MenuItem {
	
	public String name;
	public int key;
	public KeyStroke stroke;
	
	public MenuItem(String name) {
		this(name, 0);
	}
	
	public MenuItem(String name, int key) {
		this(name, key, null);
	}
	
	public MenuItem(String name, int key, KeyStroke stroke) {
		this.name = name;
		this.key = key;
		this.stroke = stroke;
	}
	
}
