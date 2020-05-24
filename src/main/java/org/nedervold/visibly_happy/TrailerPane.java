package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;

import org.nedervold.nawidgets.editor.Editor;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class TrailerPane extends CodePane implements Editor<String> {

	public TrailerPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(rows, cols, inputStream, initValue, inputLineNumberCell);
		setBorder(BorderFactory.createTitledBorder("trailer"));
	}

}
