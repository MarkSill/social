package com.marksill.social;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.marksill.social.instance.InstanceScript;

public class SocialDocumentListener implements DocumentListener {
	
	private JEditorPane textArea;
	private InstanceScript script;

	public SocialDocumentListener(JEditorPane textArea, InstanceScript script) {
		this.textArea = textArea;
		this.script = script;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		script.code = textArea.getText();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		script.code = textArea.getText();
	}

}
