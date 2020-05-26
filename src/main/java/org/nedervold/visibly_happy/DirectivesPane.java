package org.nedervold.visibly_happy;

import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import org.nedervold.nawidgets.editor.ETextField;
import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Directives;
import org.nedervold.visibly_happy.data.ToSource;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class DirectivesPane extends Box implements Editor<Directives> {

	private final ETextField expect;
	private Cell<Optional<Integer>> optExpectCell;
	final EScrollingSyntaxTextArea syntax;
	private final ETextField tokenType;

	public DirectivesPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.Y_AXIS);

		final JLabel tokenTypeLabel = new JLabel("%tokentype (a Haskell type; required)");
		tokenType = new ETextField(new Stream<String>(), "()", 20);
		final Box tokenTypeHBox = Box.createHorizontalBox();
		tokenTypeHBox.add(tokenTypeLabel);
		tokenTypeHBox.add(tokenType);
		add(tokenTypeHBox);

		final JLabel expectLabel = new JLabel("%expect (int; optional)");
		expect = new ETextField(new Stream<String>(), "", 20);
		optExpectCell = expect.outputCell().map((s) -> {
			try {
				final Integer expInt = Integer.parseUnsignedInt(s, 10);
				return Optional.of(expInt);
			} catch (final NumberFormatException nfe) {
				return Optional.empty();
			}
		});
		final Box expectHBox = Box.createHorizontalBox();
		expectHBox.add(expectLabel);
		expectHBox.add(expect);
		add(expectHBox);

		syntax = new EScrollingSyntaxTextArea(rows, cols, inputStream, initValue,
				inputLineNumberCell.lift(optExpectCell, (n, opt) -> n + (opt.isPresent() ? 2 : 1)));
		add(syntax);
		setBorder(BorderFactory.createTitledBorder("directives"));
	}

	public Cell<Integer> lineCountCell() {
		return outputCell().map(ToSource::toLineCount);
	}

	@Override
	public Cell<Directives> outputCell() {
		return tokenType.outputCell().lift(optExpectCell, syntax.outputCell(), Directives::new);

	}

}
