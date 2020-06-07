package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Grammar;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class GrammarPane extends Box implements Editor<Grammar>, Style {
	private static final String GRAMMAR_SOURCE = TextUtils.unlines(new String[] { "foo : {- empty -} {()}" });
	private final Cell<Grammar> outputCell;
	private final Cell<Integer> outputLineNumber;
	private final EScrollingSyntaxTextArea syntax;

	public GrammarPane(final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.Y_AXIS);
		syntax = new EScrollingSyntaxTextArea(ROWS, COLS, new Stream<>(), GRAMMAR_SOURCE, inputLineNumberCell);
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
