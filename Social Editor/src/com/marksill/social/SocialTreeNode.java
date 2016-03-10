package com.marksill.social;

import javax.swing.tree.DefaultMutableTreeNode;

import com.marksill.social.instance.Instance;

public class SocialTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -468818630031759929L;
	
	private Instance instance;
	
	public SocialTreeNode(String title) {
		this(title, null);
	}

	public SocialTreeNode(String title, Instance instance) {
		super(title);
		this.instance = instance;
	}
	
	public void setInstance(Instance instance) {
		this.instance = instance;
	}
	
	public Instance getInstance() {
		return instance;
	}

}
