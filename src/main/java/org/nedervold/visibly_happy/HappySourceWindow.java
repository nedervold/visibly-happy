package org.nedervold.visibly_happy;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import nz.sodium.Cell;
import nz.sodium.Transaction;

public class HappySourceWindow extends JFrame {

	private final DScrollingSyntaxTextArea src;

	public HappySourceWindow(final String title, final Cell<String> inputCell) throws HeadlessException {
		super(title);
		final Container cp = getContentPane();
		src = Transaction.run(() -> {
			final DScrollingSyntaxTextArea src = new DScrollingSyntaxTextArea(24, 80, inputCell, new Cell<>(1));
			cp.add(src);
			pack();
			return src;
		});
	}

	@Override
	public void removeNotify() {
		src.unlisten();
		super.removeNotify();
	}
}
