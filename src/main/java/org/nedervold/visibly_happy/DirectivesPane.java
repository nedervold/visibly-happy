package org.nedervold.visibly_happy;

import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import org.fife.ui.rtextarea.Gutter;
import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.editor.ETextField;
import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Directives;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class DirectivesPane extends Box implements Editor<Directives> {

	// static class NumberedLine extends Box {
	//
	// private final DLabel lineNumLabel;
	//
	// private final Cell<Integer> outputLineNumber;
	//
	// public NumberedLine(final Cell<Integer> inputLineNumber, final Cell<Boolean>
	// isPresent) {
	// super(BoxLayout.X_AXIS);
	// outputLineNumber = Cell.switchC(isPresent.map((pres) -> {
	// if (pres) {
	// return inputLineNumber.map((n) -> n + 1);
	// } else {
	// return inputLineNumber;
	// }
	// }));
	// final Cell<String> labelStr = inputLineNumber.lift(isPresent, (n, pres) -> {
	// if (pres) {
	// return String.format("%3d", n);
	// } else {
	// return String.format("%3s", "");
	// }
	// });
	// lineNumLabel = new DLabel(labelStr);
	// add(lineNumLabel);
	// add(Box.createHorizontalStrut(5));
	// }
	//
	// public Cell<Integer> getOutputLineNumber() {
	// return outputLineNumber;
	// }
	//
	// public void unlisten() {
	// lineNumLabel.unlisten();
	// }
	// }

	private final ETextField expect;
	private final DLabel lineNumLabel;
	private final DLabel lineNumLabel2;
	private Cell<Optional<Integer>> optExpectCell;
	private final Cell<Integer> outputLineNumber;
	private final EScrollingSyntaxTextArea syntax;

	private final ETextField tokenType;

	public DirectivesPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.Y_AXIS);

		lineNumLabel = new DLabel(inputLineNumberCell.map((final Integer n) -> String.format("%3d", n)));
		final JLabel tokenTypeLabel = new JLabel("%tokentype (a Haskell type; required)");
		tokenType = new ETextField(new Stream<String>(), "()", 20);
		final Box tokenTypeHBox = Box.createHorizontalBox();
		tokenTypeHBox.add(lineNumLabel);
		tokenTypeHBox.add(Box.createHorizontalStrut(5));
		tokenTypeHBox.add(tokenTypeLabel);
		tokenTypeHBox.add(tokenType);
		add(tokenTypeHBox);

		final Cell<Integer> expectLineNumber = inputLineNumberCell.map((n) -> n + 1);
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
		lineNumLabel2 = new DLabel(expectLineNumber.lift(optExpectCell, (n, opt) -> {
			if (opt.isPresent()) {
				return String.format("%3d", n);
			} else {
				return String.format("%3s", "");
			}
		}));

		final Box expectHBox = Box.createHorizontalBox();
		expectHBox.add(lineNumLabel2);
		expectHBox.add(Box.createHorizontalStrut(5));
		expectHBox.add(expectLabel);
		expectHBox.add(expect);
		add(expectHBox);

		final Cell<Integer> inLineNoForTextArea = inputLineNumberCell.lift(optExpectCell,
				(n, opt) -> n + (opt.isPresent() ? 2 : 1));
		syntax = new EScrollingSyntaxTextArea(rows, cols, inputStream, initValue, inLineNoForTextArea);

		final Gutter gutter = syntax.getGutter();
		lineNumLabel.setFont(gutter.getLineNumberFont());
		lineNumLabel.setForeground(gutter.getLineNumberColor());
		lineNumLabel2.setFont(gutter.getLineNumberFont());
		lineNumLabel2.setForeground(gutter.getLineNumberColor());

		outputLineNumber = syntax.getOutputLineNumber();
		add(syntax);
		setBorder(BorderFactory.createTitledBorder("directives"));
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	@Override
	public Cell<Directives> outputCell() {
		return tokenType.outputCell().lift(optExpectCell, syntax.outputCell(), Directives::new);

	}

	public void unlisten() {
		lineNumLabel.unlisten();
		lineNumLabel2.unlisten();
		expect.unlisten();
		syntax.unlisten();
	}

}
