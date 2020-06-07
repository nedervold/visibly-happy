package org.nedervold.visibly_happy;

import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import org.nedervold.nawidgets.editor.ETextField;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ExpectDirective extends Box implements Style {
	private final ETextField expect;
	private final LineNumLabel lineNumLabel;
	private final Cell<Optional<Integer>> optExpectCell;
	private final Cell<Integer> outputLineNumber;

	// TODO I can restrict the characters to digits, can't I? Then I don't need the
	// color of EColorTextField.
	public ExpectDirective(final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.X_AXIS);
		final JLabel expectLabel = new JLabel("%expect (int; optional)");
		expect = new EColorTextField(new Stream<String>(), "", 20) {
			@Override
			protected boolean isValidInput(final String input) {
				try {
					Integer.parseUnsignedInt(input.trim());
					return true;
				} catch (final NumberFormatException nfe) {
					return false;
				}
			}
		};
		optExpectCell = expect.outputCell().map((s) -> {
			try {
				final Integer expInt = Integer.parseUnsignedInt(s.trim(), 10);
				return Optional.of(expInt);
			} catch (final NumberFormatException nfe) {
				return Optional.empty();
			}
		});
		final Cell<Optional<Integer>> optLineNumber = inputLineNumberCell.lift(optExpectCell, (n, opt) -> {
			if (opt.isPresent()) {
				return Optional.of(n);
			} else {
				return Optional.empty();
			}
		});
		lineNumLabel = new LineNumLabel(optLineNumber);
		outputLineNumber = inputLineNumberCell.lift(optExpectCell, (n, opt) -> n + (opt.isPresent() ? 1 : 0));

		this.add(lineNumLabel);
		this.add(Box.createHorizontalStrut(5));
		this.add(expectLabel);
		this.add(expect);
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	public Cell<Optional<Integer>> outputCell() {
		return optExpectCell;
	}

	public void unlisten() {
		expect.unlisten();
		lineNumLabel.unlisten();
	}
}