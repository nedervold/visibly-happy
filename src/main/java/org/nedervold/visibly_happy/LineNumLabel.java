package org.nedervold.visibly_happy;

import java.util.Optional;

import org.nedervold.nawidgets.display.DLabel;

import nz.sodium.Cell;

public class LineNumLabel extends DLabel implements Style {

	public static String f(final Optional<Integer> optLineNum) {
		if (optLineNum.isPresent()) {
			return String.format("%3d", optLineNum.get());
		} else {
			return String.format("%3s", "");
		}
	}

	public LineNumLabel(final Cell<Optional<Integer>> inputCell) {
		super(inputCell.map(LineNumLabel::f));
		matchGutter();
	}

	private void matchGutter() {
		setFont(GUTTER.getLineNumberFont());
		setForeground(GUTTER.getLineNumberColor());
	}

}
