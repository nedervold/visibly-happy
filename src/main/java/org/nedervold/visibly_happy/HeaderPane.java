package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class HeaderPane extends CodePane {

	private static final String HEADER_SOURCE = TextUtils.unlines(new String[] { "module Parser where" });

	public HeaderPane(final Cell<Integer> inputLineNumberCell) {
		super(new Stream<>(), HEADER_SOURCE, inputLineNumberCell);
		setBorder(BorderFactory.createTitledBorder("header"));
	}

}
