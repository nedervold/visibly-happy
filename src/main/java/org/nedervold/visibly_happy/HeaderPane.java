package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class HeaderPane extends CodePane {

	public HeaderPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(rows, cols, inputStream, initValue, inputLineNumberCell);
		setBorder(BorderFactory.createTitledBorder("header"));
	}

}