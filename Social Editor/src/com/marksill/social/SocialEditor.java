package com.marksill.social;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.newdawn.slick.SlickException;

public class SocialEditor extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2541131438666062756L;
	
	public SocialEditor(String[] args) {
		Social social = new Social();
		try {
			social.start(true, true, args);
		} catch (SocialException e) {
			e.printStackTrace();
		}
		add(social.getCanvasContainer());
		
		//Toolbar
		JToolBar toolbar = new JToolBar();
		JButton fileButton = new JButton("File");
		fileButton.addActionListener(this);
		toolbar.add(fileButton);
		add(toolbar, BorderLayout.PAGE_START);
		pack();
		
		setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Social Editor");
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
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
		
		try {
			social.getCanvasContainer().start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new SocialEditor(args);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
	}

}
