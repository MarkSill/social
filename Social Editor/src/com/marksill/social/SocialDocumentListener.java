package com.marksill.social;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.marksill.social.instance.InstanceScript;

public class SocialDocumentListener implements DocumentListener {
	
	private RSyntaxTextArea textArea;
	private InstanceScript script;

	public SocialDocumentListener(RSyntaxTextArea textArea, InstanceScript script) {
		this.textArea = textArea;
		this.script = script;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		update(e);
	}
	
	public void update(DocumentEvent e) {
		String text = textArea.getText();
		script.code = text;
	}

}
