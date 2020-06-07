package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.fife.ui.rtextarea.Gutter;
import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Directives;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class DirectivesPane extends Box implements Editor<Directives> {

	public static class DirectivesTextArea extends EScrollingSyntaxTextArea implements Style {
		public DirectivesTextArea(final Cell<Integer> inputLineNumberCell) {
			super(ROWS, COLS, new Stream<>(), DIRECTIVES_SOURCE, inputLineNumberCell);
		}
	}

	private static final String DIRECTIVES_SOURCE = TextUtils.unlines(new String[] { "%token UNIT {$$}" });
	private final ExpectDirective expectDirective;
	private final Cell<Integer> outputLineNumber;
	private final EScrollingSyntaxTextArea syntax;

	private final TokentypeDirective tokentypeDirective;

	public DirectivesPane(final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.Y_AXIS);

		tokentypeDirective = new TokentypeDirective(inputLineNumberCell);
		expectDirective = new ExpectDirective(tokentypeDirective.getOutputLineNumber());
		syntax = new DirectivesTextArea(expectDirective.getOutputLineNumber());
		outputLineNumber = syntax.getOutputLineNumber();

		add(tokentypeDirective);
		add(expectDirective);
		add(syntax);

		final Gutter gutter = syntax.getGutter();
		tokentypeDirective.matchGutter(gutter);
		expectDirective.matchGutter(gutter);

		setBorder(BorderFactory.createTitledBorder("directives"));
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	@Override
	public Cell<Directives> outputCell() {
		return tokentypeDirective.outputCell().lift(expectDirective.outputCell(), syntax.outputCell(), Directives::new);

	}

	public void unlisten() {
		tokentypeDirective.unlisten();
		expectDirective.unlisten();
		syntax.unlisten();
	}

}
