package org.nedervold.visibly_happy;

import org.nedervold.nawidgets.display.DWidgetImpl;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class BracedPane extends EScrollingSyntaxTextArea {

	private static class LineNumbersEnabledImpl extends DWidgetImpl<BracedPane, Boolean> {
		public LineNumbersEnabledImpl(final BracedPane component, final Cell<Boolean> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Boolean value) {
			component.setLineNumbersEnabled(value);
		}
	}

	private final LineNumbersEnabledImpl lineNumbersEnabledImpl;
	private final Cell<Integer> outputLineNumber;

	public BracedPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		super(rows, cols, inputStream, initValue, inputLineNumberCell.map((n) -> n + 1));
		final Cell<Boolean> enabledCell = super.outputCell().map((s) -> !s.isEmpty());
		lineNumbersEnabledImpl = new LineNumbersEnabledImpl(this, enabledCell);
		outputLineNumber = super.getOutputLineNumber().map((n) -> n + 1);
	}

	@Override
	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	public void unlisten() {
		lineNumbersEnabledImpl.unlisten();
	}
}
