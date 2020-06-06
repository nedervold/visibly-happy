package org.nedervold.visibly_happy;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.Transaction;

public class HappySourceWindow extends JFrame {

	private final EScrollingSyntaxTextArea src;

	public HappySourceWindow(final String title, final Stream<String> inputStream) throws HeadlessException {
		super(title);
		final Container cp = getContentPane();
		src = Transaction.run(() -> {
			final EScrollingSyntaxTextArea src = new EScrollingSyntaxTextArea(24, 80, inputStream, "", new Cell<>(1));
			src.syntaxTextArea.setEditable(false);
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
