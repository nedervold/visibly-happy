package org.nedervold.visibly_happy;

import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import org.nedervold.nawidgets.editor.ETextField;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class TokentypeDirective extends Box implements Style {
	private final LineNumLabel lineNumLabel;
	private final Cell<Integer> outputLineNumber;
	private final ETextField tokentypeTextField;

	public TokentypeDirective(final Cell<Integer> inputLineNumberCell) {
		super(BoxLayout.X_AXIS);
		lineNumLabel = new LineNumLabel(inputLineNumberCell.map(Optional::of));
		final JLabel tokenTypeLabel = new JLabel("%tokentype (a Haskell type; required)");
		tokentypeTextField = new ETextField(new Stream<String>(), "()", 20);
		outputLineNumber = inputLineNumberCell.map((n) -> n + 1);

		add(lineNumLabel);
		add(Box.createHorizontalStrut(5));
		add(tokenTypeLabel);
		add(tokentypeTextField);
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	public Cell<String> outputCell() {
		return tokentypeTextField.outputCell();
	}

	public void unlisten() {
		lineNumLabel.unlisten();
		tokentypeTextField.unlisten();
	}
}