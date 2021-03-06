package com.marksill.social;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SocialTreeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -213789301334007621L;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
		SocialTreeNode node = (SocialTreeNode) value;
		String className = "null";
		String loc = "error";
		if (node.getInstance() != null) {
			className = node.getInstance().getClass().getSimpleName();
		}
		switch (className) {
		case "Instance":
			loc = "tag";
			break;
		case "InstanceBlock": case "InstanceCircle": case "InstanceRectangle":
			loc = "brick";
			break;
		case "InstanceCamera":
			loc = "camera";
			break;
		case "InstanceContainer":
			loc = "folder";
			break;
		case "InstanceEvent":
			loc = "lightning";
			break;
		case "InstanceGame":
			loc = "package";
			break;
		case "InstanceImages":
			loc = "images";
			break;
		case "InstanceJoints":
			loc = "connect";
			break;
		case "InstancePlayer":
			loc = "user";
			break;
		case "InstancePlayers":
			loc = "folder_user";
			break;
		case "InstanceScript": case "InstanceClientScript":
			loc = "script";
			break;
		case "InstanceWorld":
			loc = "world";
			break;
		}
		ImageIcon icon = new ImageIcon("icons/" + loc + ".png");
		setOpenIcon(icon);
		setLeafIcon(icon);
		setClosedIcon(icon);
		setDisabledIcon(icon);
		super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
		return this;
	}

}
