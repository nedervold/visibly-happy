package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Grammar;
import org.nedervold.visibly_happy.data.ToSource;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class GrammarPane extends Box implements Editor<Grammar> {
	public EScrollingSyntaxTextArea syntax;

	public GrammarPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.Y_AXIS);
		syntax = new EScrollingSyntaxTextArea(rows, cols, inputStream, initValue, inputLineNumberCell);

		final Cell<Integer> gram = syntax.outputCell().map(Grammar::new).map(ToSource::toLineCount);
		final Cell<String> gramLineCount = gram.map((g) -> "grammar line count = " + g);
		final DLabel gramLineLabel = new DLabel(gramLineCount);
		add(gramLineLabel);
		add(syntax);
		setBorder(BorderFactory.createTitledBorder("grammar"));
	}

	public Cell<Integer> lineCountCell() {
		return outputCell().map(ToSource::toLineCount);
	}

	@Override
	public Cell<Grammar> outputCell() {
		return syntax.outputCell().map(Grammar::new);
	}

}
