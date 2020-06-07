package org.nedervold.visibly_happy;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.nedervold.nawidgets.display.DWidgetImpl;

import nz.sodium.Cell;

public class DScrollingSyntaxTextArea extends RTextScrollPane {
	public class LineNumberImpl extends DWidgetImpl<DScrollingSyntaxTextArea, Integer> {
		public LineNumberImpl(final DScrollingSyntaxTextArea component, final Cell<Integer> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Integer value) {
			component.getGutter().setLineNumberingStartIndex(value);
			component.repaint();
		}
	}

	private static DSyntaxTextArea createComponent(final int rows, final int cols, final Cell<String> inputCell) {
		return new DSyntaxTextArea(rows, cols, inputCell);
	}

	private final Cell<Integer> lineCount;
	private final LineNumberImpl lineNumberImpl;
	private final Cell<Integer> outputLineNumber;
	private final DSyntaxTextArea syntaxTextArea;

	private DScrollingSyntaxTextArea(final DSyntaxTextArea syntaxTextArea, final Cell<String> inputCell,
			final Cell<Integer> inputLineNumberCell) {
		super(syntaxTextArea);
		this.syntaxTextArea = syntaxTextArea;
		lineNumberImpl = new LineNumberImpl(this, inputLineNumberCell);
		lineCount = inputCell.map((str) -> TextUtils.countLines(TextUtils.ensureFinalNewline(str)));
		outputLineNumber = inputLineNumberCell.lift(lineCount, (in, mid) -> in + mid);
		setLineNumbersEnabled(true);
	}

	public DScrollingSyntaxTextArea(final int rows, final int cols, final Cell<String> inputCell,
			final Cell<Integer> inputLineNumberCell) {
		this(createComponent(rows, cols, inputCell), inputCell, inputLineNumberCell);
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	public void unlisten() {
		syntaxTextArea.unlisten();
		lineNumberImpl.unlisten();
	}

}
