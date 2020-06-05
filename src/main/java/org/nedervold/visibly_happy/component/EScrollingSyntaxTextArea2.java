package org.nedervold.visibly_happy.component;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.nedervold.nawidgets.display.DWidgetImpl;
import org.nedervold.visibly_happy.ESyntaxTextArea;
import org.nedervold.visibly_happy.monad.State;
import org.nedervold.visibly_happy.new_data.Lines;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import nz.sodium.Cell;
import nz.sodium.Stream;

public class EScrollingSyntaxTextArea2 extends RTextScrollPane
		implements SourceComponent<EScrollingSyntaxTextArea2, Lines> {

	public class LineNumberImpl extends DWidgetImpl<EScrollingSyntaxTextArea2, Integer> {

		public LineNumberImpl(final EScrollingSyntaxTextArea2 component, final Cell<Integer> inputCell) {
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

	public static State<Cell<Integer>, Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>>> FACTORY(final int rows,
			final int cols) {
		return (beforeLineCount) -> {
			final EScrollingSyntaxTextArea2 esta = new EScrollingSyntaxTextArea2(rows, cols, beforeLineCount);

			final Cell<Lines> data = esta.data();
			final Cell<Integer> internalLines = data.map(Lines::size);
			final Cell<Integer> afterLineCount = beforeLineCount.lift(internalLines,
					(before, internal) -> before + internal);
			return Tuple.of(afterLineCount, Tuple.of(esta, data));
		};
	}

	protected final LineNumberImpl lineNumberImpl;

	protected final ESyntaxTextArea syntaxTextArea;

	private EScrollingSyntaxTextArea2(final ESyntaxTextArea syntaxTextArea, final Cell<Integer> inputLineNumberCell) {
		super(syntaxTextArea);
		this.syntaxTextArea = syntaxTextArea;
		lineNumberImpl = new LineNumberImpl(this, inputLineNumberCell);
		setLineNumbersEnabled(true);
	}

	public EScrollingSyntaxTextArea2(final int rows, final int cols, final Cell<Integer> startingLineCount) {
		this(rows, cols, new Stream<>(), "", startingLineCount);
	}

	public EScrollingSyntaxTextArea2(final int rows, final int cols, final Stream<String> inputStream,
			final String initValue, final Cell<Integer> inputLineNumberCell) {
		this(createComponent(rows, cols, inputStream, initValue), inputLineNumberCell);
	}

	@Override
	public Cell<Lines> data() {
		return syntaxTextArea.outputCell().map(Lines::new);
	}

	@Override
	public EScrollingSyntaxTextArea2 swingComponent() {
		return this;
	}

	// TODO Where is the access to the listener?
}
