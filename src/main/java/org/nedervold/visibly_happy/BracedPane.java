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

	private static class VisibleImpl extends DWidgetImpl<BracedPane, Boolean> {
		public VisibleImpl(final BracedPane component, final Cell<Boolean> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Boolean value) {
			component.setVisible(value);
			component.revalidate();
		}
	}

	private final LineNumbersEnabledImpl lineNumbersEnabledImpl;
	private final VisibleImpl visibleImpl;

	public BracedPane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell, final Cell<Boolean> visibleCell) {
		super(rows, cols, inputStream, initValue, inputLineNumberCell.map((n) -> n + 1));
		final Cell<Boolean> enabledCell = super.outputCell().map((s) -> !s.isEmpty());
		lineNumbersEnabledImpl = new LineNumbersEnabledImpl(this, enabledCell);
		visibleImpl = new VisibleImpl(this, visibleCell);
	}

	@Override
	public Cell<String> outputCell() {
		return super.outputCell();
	}

	public void unlisten() {
		lineNumbersEnabledImpl.unlisten();
		visibleImpl.unlisten();
	}
}
