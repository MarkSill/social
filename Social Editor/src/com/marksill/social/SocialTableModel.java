package com.marksill.social;

import javax.swing.table.DefaultTableModel;

public class SocialTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -4151838646715823512L;
	
	public SocialTableModel() {
		super(new Object[] {"Property", "Value"}, 0);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 0) {
			return false;
		}
		String val = (String) getValueAt(row, 0);
		switch (val) {
		case "ClassName":
		case "Running":
		case "Code":
			return false;
		}
		return true;
	}
	
	public void removeAllRows() {
		for (int i = getRowCount() - 1; i >= 0; i--) {
			removeRow(i);
		}
	}

}
