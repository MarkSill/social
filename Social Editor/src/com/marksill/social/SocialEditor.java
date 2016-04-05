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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceBlock;
import com.marksill.social.instance.InstanceCircle;
import com.marksill.social.instance.InstanceGame;
import com.marksill.social.instance.InstancePlayers;
import com.marksill.social.instance.InstanceRectangle;
import com.marksill.social.instance.InstanceScript;
import com.marksill.social.instance.InstanceWorld;

public class SocialEditor extends JFrame implements ActionListener, KeyListener, TreeSelectionListener, TreeModelListener, CellEditorListener, TableModelListener {

	private static final long serialVersionUID = 2541131438666062756L;
	
	public static SocialEditor editor;
	
	private JTree tree;
	private SocialTreeNode rootNode;
	private JTable properties;
	private Map<Instance, SocialTreeNode> map, lastMap;
	private JTabbedPane contentPane;
	
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
		properties = new JTable(new SocialTableModel());
		properties.getModel().addTableModelListener(this);
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
		contentPane = new JTabbedPane();
		JPanel gamePane = new JPanel(new BorderLayout());
		gamePane.add(container, BorderLayout.CENTER);
		contentPane.setMinimumSize(new Dimension(10, 10));
		contentPane.addTab("Game", gamePane);
		
		//Split pane that separates the game from instances and properties
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, browser, contentPane);
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
				new MenuItem("Container", KeyEvent.VK_O),
				new MenuItem("Rectangle", KeyEvent.VK_R),
				new MenuItem("Circle", KeyEvent.VK_C),
				new MenuItem("Game", KeyEvent.VK_G),
				new MenuItem("Script", KeyEvent.VK_S),
				new MenuItem("World", KeyEvent.VK_W)
		});
		menu.setMnemonic(KeyEvent.VK_I);
		menu = createMenu(menubar, "Test", new MenuItem[] {
				new MenuItem("Play/Pause", KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)),
				//new MenuItem("Stop", KeyEvent.VK_S, KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0))
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
			if (contentPane.getSelectedIndex() == 0) {
				if (Instance.game != null) {
					Instance.game.delete();
					Instance.game = null;
				}
			} else {
				int index = contentPane.getSelectedIndex();
				List<Instance> scripts = Instance.findInstances(Instance.game, InstanceScript.class);
				for (Instance instance : scripts) {
					InstanceScript script = (InstanceScript) instance;
					if (script.tabIndex == index) {
						script.tabIndex = -1;
					}
				}
				contentPane.removeTabAt(index);
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
					Instance instance = node.getInstance();
					if (node != null && instance != null) {
						Instance.selected.remove(instance);
						if (instance instanceof InstanceScript) {
							InstanceScript script = (InstanceScript) instance;
							if (script.tabIndex != -1) {
								contentPane.removeTabAt(script.tabIndex);
							}
						}
						instance.delete();
						SocialTableModel model = (SocialTableModel) properties.getModel();
						model.removeAllRows();
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
		case "Container":
			inst = "container";
			break;
		case "Rectangle":
			inst = "rectangle";
			break;
		case "Circle":
			inst = "circle";
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
		Instance.selected.clear();
		TreePath[] paths = tree.getSelectionPaths();
		if (paths == null) return;
		for (TreePath path : paths) {
			SocialTreeNode node = (SocialTreeNode) path.getLastPathComponent();
			properties.removeAll();
			Instance inst = node.getInstance();
			if (inst != null) {
				Instance.selected.add(inst);
				String className = inst.getClass().getSimpleName();
				Object[][] values = new Object[][] {
					{"Name", inst.name},
					{"ClassName", className.substring(8)}
				};
				switch (className) {
				case "InstanceCircle":
				case "InstanceRectangle":
				case "InstanceBlock":
					InstanceBlock block = (InstanceBlock) inst;
					Object size = block.getBody().getFixture(0).getShape();
					if (size instanceof Rectangle) {
						size = new Vector2(((Rectangle) size).getWidth(), ((Rectangle) size).getHeight());
					} else {
						size = ((Convex) size).getRadius();
					}
					values = mergeValues(values, new Object[][] {
						{"Position X", block.position.x},
						{"Position Y", block.position.y},
						{"Anchored", block.anchored},
						{"Color Red", block.color.r * 255},
						{"Color Green", block.color.g * 255},
						{"Color Blue", block.color.b * 255},
						{"Color Alpha", block.color.a * 255},
						{"Visible", block.visible},
						{"Mass", block.mass},
						{"Density", block.density},
						{"Elasticity", block.elasticity},
						{"Friction", block.friction}
					});
					if (block instanceof InstanceRectangle) {
						InstanceRectangle rect = (InstanceRectangle) block;
						values = mergeValues(values, new Object[][] {
							{"Size X", rect.size.x},
							{"Size Y", rect.size.y}
						});
					} else if (block instanceof InstanceCircle) {
						InstanceCircle circ = (InstanceCircle) block;
						values = mergeValues(values, new Object[][] {
							{"Radius", circ.radius}
						});
					}
					break;
				case "InstanceGame":
					//InstanceGame game = (InstanceGame) inst;
					break;
				case "InstancePlayers":
					InstancePlayers players = (InstancePlayers) inst;
					values = mergeValues(values, new Object[][] {
						{"Max Players", players.maxPlayers}
					});
					break;
				case "InstanceScript":
					InstanceScript script = (InstanceScript) inst;
					values = mergeValues(values, new Object[][] {
						{"Enabled", script.enabled},
						{"Running", script.running},
						{"Code", script.code}
					});
					
					if (paths.length == 1 && script.tabIndex == -1) {
						RSyntaxTextArea scriptArea = new RSyntaxTextArea();
						scriptArea.setCodeFoldingEnabled(true);
						JScrollPane scriptPane = new JScrollPane(scriptArea);
						scriptArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
						scriptArea.setText(script.code);
						scriptArea.getDocument().addDocumentListener(new SocialDocumentListener(scriptArea, script));
						contentPane.addTab(inst.name, scriptPane);
						script.tabIndex = contentPane.getTabCount() - 1;
						contentPane.setSelectedIndex(script.tabIndex);
					}
					break;
				case "InstanceWorld":
					InstanceWorld world = (InstanceWorld) inst;
					values = mergeValues(values, new Object[][] {
						{"Gravity X", world.gravX},
						{"Gravity Y", world.gravY}
					});
					break;
				}
				
				TableModel tmodel = properties.getModel();
				SocialTableModel model = (SocialTableModel) tmodel;
				model.removeAllRows();
				for (Object[] row : values) {
					model.addRow(row);
				}
			}
		}
	}
	
	public Object[][] mergeValues(Object[][] values, Object[][] newValues) {
		Object[][] copy = Arrays.copyOf(values, values.length + newValues.length);
		for (int i = 0; i < newValues.length; i++) {
			copy[values.length + i] = newValues[i];
		}
		return copy;
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
		
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		Object src = e.getSource();
		if (src instanceof TreeCellEditor) {
			TreeCellEditor edit = (TreeCellEditor) src;
			SocialTreeNode node = (SocialTreeNode) tree.getSelectionPath().getLastPathComponent();
			if (node.getInstance() != null) {
				node.getInstance().name = (String) edit.getCellEditorValue();
			}
		} else {
			System.out.println(e);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			SocialTableModel model = (SocialTableModel) properties.getModel();
			Object newValue = model.getValueAt(row, col);
			String key = (String) model.getValueAt(row, 0);
			Instance inst = ((SocialTreeNode) tree.getSelectionPath().getLastPathComponent()).getInstance();
			switch (key) {
			case "Name":
				inst.name = (String) newValue;
				buildTree();
				tree.startEditingAtPath(tree.getSelectionPath());
				tree.stopEditing();
				break;
			case "Max Players":
				((InstancePlayers) inst).maxPlayers = Integer.parseInt((String) newValue);
				break;
			case "Anchored":
				((InstanceBlock) inst).anchored = Boolean.parseBoolean((String) newValue);
				break;
			case "Visible":
				((InstanceBlock) inst).visible = Boolean.parseBoolean((String) newValue);
				break;
			case "Position X":
				((InstanceBlock) inst).position = new Vector2(Double.parseDouble((String) newValue), ((InstanceBlock) inst).position.y);
				break;
			case "Position Y":
				((InstanceBlock) inst).position = new Vector2(((InstanceBlock) inst).position.x, Double.parseDouble((String) newValue));
				break;
			case "Color Red":
				((InstanceBlock) inst).color.r = Float.parseFloat((String) newValue) / 255;
				break;
			case "Color Green":
				((InstanceBlock) inst).color.g = Float.parseFloat((String) newValue) / 255;
				break;
			case "Color Blue":
				((InstanceBlock) inst).color.b = Float.parseFloat((String) newValue) / 255;
				break;
			case "Color Alpha":
				((InstanceBlock) inst).color.a = Float.parseFloat((String) newValue) / 255;
				break;
			case "Mass":
				((InstanceBlock) inst).mass = Double.parseDouble((String) newValue);
				break;
			case "Density":
				((InstanceBlock) inst).density = Double.parseDouble((String) newValue);
				break;
			case "Elasticity":
				((InstanceBlock) inst).elasticity = Double.parseDouble((String) newValue);
				break;
			case "Friction":
				((InstanceBlock) inst).friction = Double.parseDouble((String) newValue);
				break;
			case "Radius":
				((InstanceCircle) inst).radius = Double.parseDouble((String) newValue);
				break;
			case "Size X":
				((InstanceRectangle) inst).size.x = Double.parseDouble((String) newValue);
				break;
			case "Size Y":
				((InstanceRectangle) inst).size.y = Double.parseDouble((String) newValue);
				break;
			case "Gravity X":
				((InstanceWorld) inst).gravX = Double.parseDouble((String) newValue);
				break;
			case "Gravity Y":
				((InstanceWorld) inst).gravY = Double.parseDouble((String) newValue);
				break;
			}
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
