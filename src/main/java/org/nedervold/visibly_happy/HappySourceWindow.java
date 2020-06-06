package org.nedervold.visibly_happy;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import nz.sodium.Cell;
import nz.sodium.Operational;
import nz.sodium.Transaction;

public class HappySourceWindow extends JFrame {

	private final EScrollingSyntaxTextArea src;

	public HappySourceWindow(final String title, final Cell<String> inputCell) throws HeadlessException {
		super(title);
		final Container cp = getContentPane();
		src = Transaction.run(() -> {
			// This should really be a DScrollingSyntaxTextArea.
			final EScrollingSyntaxTextArea src = new EScrollingSyntaxTextArea(24, 80, Operational.updates(inputCell),
					inputCell.sample(), new Cell<>(1));
			src.setEditable(false);
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
