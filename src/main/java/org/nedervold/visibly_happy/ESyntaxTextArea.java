package org.nedervold.visibly_happy;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.nedervold.nawidgets.editor.ETextComponentImpl;
import org.nedervold.nawidgets.editor.Editor;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class ESyntaxTextArea extends RSyntaxTextArea implements Editor<String> {
	static public class Impl extends ETextComponentImpl<ESyntaxTextArea> {

		public Impl(final ESyntaxTextArea comp, final Stream<String> inp, final String init) {
			super(comp, inp, init);
		}
	}

	private final Impl impl;

	public ESyntaxTextArea(final int rows, final int cols, final Stream<String> inputStream, final String initValue) {
		super(initValue, rows, cols);
		setHighlightCurrentLine(false);
		impl = new Impl(this, inputStream, initValue);
	}

	@Override
	public Cell<String> outputCell() {
		return impl.outputCell;
	}

	public void unlisten() {
		impl.unlisten();
	}
}
