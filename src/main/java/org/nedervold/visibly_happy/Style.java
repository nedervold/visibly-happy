package org.nedervold.visibly_happy;

import nz.sodium.StreamSink;

public interface Style {

	public static final int COLS = 80;
	public static final StreamSink<String> NEVER = new StreamSink<>();
	public static final int ROWS = 12;

}