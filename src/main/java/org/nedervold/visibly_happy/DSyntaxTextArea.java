package org.nedervold.visibly_happy;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.nedervold.nawidgets.display.DWidgetImpl;

import nz.sodium.Cell;

public class DSyntaxTextArea extends RSyntaxTextArea {
	public static class Impl extends DWidgetImpl<DSyntaxTextArea, String> {

		public Impl(final DSyntaxTextArea component, final Cell<String> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final String value) {
			component.setText(value);
		}
	}

	private final Impl impl;

	public DSyntaxTextArea(final int rows, final int cols, final Cell<String> inputCell) {
		super(inputCell.sample(), rows, cols);
		impl = new Impl(this, inputCell);
		setEditable(false);
		// setClearWhitespaceLinesEnabled(true);
		setHighlightCurrentLine(false);
		// setWhitespaceVisible(false);
	}

	public void unlisten() {
		impl.unlisten();
	}
}
