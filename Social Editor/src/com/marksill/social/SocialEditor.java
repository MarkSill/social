package com.marksill.social;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.SlickException;

import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceGame;

public class SocialEditor extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 2541131438666062756L;
	
	public static SocialEditor editor;
	
	/**
	 * @param args
	 */
	public SocialEditor(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Social social = new Social();
		try {
			social.start(true, true, args);
		} catch (SocialException e) {
			e.printStackTrace();
		}
		
		JScrollPane browser = new JScrollPane();
		JPanel gamePane = new JPanel();
		//gamePane.set
		gamePane.add(social.getCanvasContainer());
		gamePane.setBorder(null);
		social.getCanvasContainer().setSize(800, 600);
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, browser, social.getCanvasContainer());
		pane.setOneTouchExpandable(false);
		pane.setDividerLocation(150);
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
			new MenuItem("Paste", KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK))
		});
		menu.setMnemonic(KeyEvent.VK_E);
		add(menubar, BorderLayout.PAGE_START);
		pack();
		
		//setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Social Editor");
		setLocationRelativeTo(null);
		addKeyListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				social.getCanvasContainer().dispose();
			}
			
			@Override
			public void windowClosed(WindowEvent event) {
				System.exit(0);
			}
		});
		setVisible(true);
		
		try {
			social.getCanvasContainer().start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		editor = new SocialEditor(args);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch (cmd) {
		case "New":
			System.out.println("It works!");
			Instance.game = new InstanceGame();
			break;
		case "Open...":
			break;
		case "Close":
			Instance.game = null;
			break;
		case "Save":
			break;
		case "Save As...":
			break;
		case "Exit":
			dispose();
			break;
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
	
	public void dispatchAnEvent(AWTEvent event) {
		processEvent(event);
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
