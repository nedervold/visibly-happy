package org.nedervold.visibly_happy;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.nedervold.nawidgets.display.DWidgetImpl;
import org.nedervold.nawidgets.editor.Editor;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class EScrollingSyntaxTextArea extends RTextScrollPane implements Editor<String> {
	public class LineNumberImpl extends DWidgetImpl<EScrollingSyntaxTextArea, Integer> {

		public LineNumberImpl(final EScrollingSyntaxTextArea component, final Cell<Integer> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Integer value) {
			component.getGutter().setLineNumberingStartIndex(value);
			component.repaint();
		}

	}

	private static ESyntaxTextArea createComponent(final int rows, final int cols, final Stream<String> inputStream,
			final String initValue) {
		return new ESyntaxTextArea(rows, cols, inputStream, initValue);
	}

	private final Cell<Integer> lineCount;
	protected final LineNumberImpl lineNumberImpl;
	private final Cell<Integer> outputLineNumber;
	protected final ESyntaxTextArea syntaxTextArea;

	private EScrollingSyntaxTextArea(final ESyntaxTextArea syntaxTextArea, final Cell<Integer> inputLineNumberCell) {
		super(syntaxTextArea);
		this.syntaxTextArea = syntaxTextArea;
		lineNumberImpl = new LineNumberImpl(this, inputLineNumberCell);
		lineCount = outputCell().map((str) -> TextUtils.countLines(TextUtils.ensureFinalNewline(str)));
		outputLineNumber = inputLineNumberCell.lift(lineCount, (in, mid) -> in + mid);
		setLineNumbersEnabled(true);
	}

	public EScrollingSyntaxTextArea(final int rows, final int cols, final Stream<String> inputStream,
			final String initValue, final Cell<Integer> inputLineNumberCell) {
		this(createComponent(rows, cols, inputStream, initValue), inputLineNumberCell);
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	public Cell<Integer> lineCountCell() {
		return lineCount;
	}

	@Override
	public Cell<String> outputCell() {
		return syntaxTextArea.outputCell();
	}

	public void unlisten() {
		syntaxTextArea.unlisten();
		lineNumberImpl.unlisten();
	}

}
