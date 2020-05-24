package org.nedervold.visibly_happy;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.Transaction;

public class HappySourceWindow extends JFrame {

	public HappySourceWindow(String title, Stream<String> inputStream) throws HeadlessException {
		super(title);
		Container cp = this.getContentPane();
		Transaction.runVoid(() -> {
			EScrollingSyntaxTextArea src = new EScrollingSyntaxTextArea(24, 80, inputStream, "", new Cell<Integer>(1));
			src.syntaxTextArea.setEditable(false);
			cp.add(src);
			pack();
		});
	}

}
