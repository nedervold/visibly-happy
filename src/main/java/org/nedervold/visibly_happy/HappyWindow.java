package org.nedervold.visibly_happy;

import java.awt.HeadlessException;

import javax.swing.JFrame;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class HappyWindow extends JFrame {

	public HappyWindow(final String title) throws HeadlessException {
		super(title);
		getContentPane().add(new RSyntaxTextArea(30, 80));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

}
