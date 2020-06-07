package org.nedervold.visibly_happy;

import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.editor.ETextField;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ExpectDirective extends Box implements Style {
	private final ETextField expect;
	private final DLabel lineNumLabel;
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
		lineNumLabel = new DLabel(inputLineNumberCell.lift(optExpectCell, (n, opt) -> {
			if (opt.isPresent()) {
				return String.format("%3d", n);
			} else {
				return String.format("%3s", "");
			}
		}));
		matchGutter();
		outputLineNumber = inputLineNumberCell.lift(optExpectCell, (n, opt) -> n + (opt.isPresent() ? 1 : 0));

		this.add(lineNumLabel);
		this.add(Box.createHorizontalStrut(5));
		this.add(expectLabel);
		this.add(expect);
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	private void matchGutter() {
		lineNumLabel.setFont(GUTTER.getLineNumberFont());
		lineNumLabel.setForeground(GUTTER.getLineNumberColor());
	}

	public Cell<Optional<Integer>> outputCell() {
		return optExpectCell;
	}

	public void unlisten() {
		expect.unlisten();
		lineNumLabel.unlisten();
	}
}