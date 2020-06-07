package org.nedervold.visibly_happy;

import javax.swing.BorderFactory;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class TrailerPane extends CodePane {

	private final static String TRAILER_SOURCE = "";

	public TrailerPane(final Cell<Integer> inputLineNumberCell) {
		super(new Stream<>(), TRAILER_SOURCE, inputLineNumberCell);
		setBorder(BorderFactory.createTitledBorder("trailer"));
	}

}
