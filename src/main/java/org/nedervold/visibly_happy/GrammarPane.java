package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Grammar;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class GrammarPane extends Box implements Editor<Grammar> {
	private final Cell<Grammar> outputCell;
	private final Cell<Integer> outputLineNumber;
	private final EScrollingSyntaxTextArea syntax;

	public GrammarPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.Y_AXIS);
		syntax = new EScrollingSyntaxTextArea(rows, cols, inputStream, initValue, inputLineNumberCell);
		add(syntax);
		setBorder(BorderFactory.createTitledBorder("grammar"));
		outputCell = syntax.outputCell().map(Grammar::new);
		outputLineNumber = syntax.getOutputLineNumber();
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	@Override
	public Cell<Grammar> outputCell() {
		return outputCell;
	}

	public void unlisten() {
		syntax.unlisten();
	}

}
