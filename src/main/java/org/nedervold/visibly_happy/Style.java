package org.nedervold.visibly_happy;

import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import nz.sodium.StreamSink;

public interface Style {

	public static final int COLS = 80;
	public static Gutter GUTTER = new RTextScrollPane().getGutter();
	/**
	 * The narrowest I can make a button and retain the macOS LnF.
	 */
	public static final int MAC_OS_MIN_BUTTON_WIDTH = 40;

	public static final StreamSink<String> NEVER = new StreamSink<>();
	public static final int ROWS = 12;

}