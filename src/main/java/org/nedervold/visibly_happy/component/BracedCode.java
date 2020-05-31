package org.nedervold.visibly_happy.component;

import javax.swing.JComponent;

import org.nedervold.visibly_happy.monad.State;
import org.nedervold.visibly_happy.new_data.Lines;

import io.vavr.Function3;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import nz.sodium.Cell;

public class BracedCode implements SourceComponent<EScrollingSyntaxTextArea2, Lines> {

	public static State<Cell<Integer>, Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>>> FACTORY(final int rows,
			final int cols) {
		final Function3<Tuple2<JComponent, Cell<Lines>>, Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>>, Tuple2<JComponent, Cell<Lines>>, Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>>> f = (
				leftBracePair, textPair, rightBracePair) -> {
			final BracedCode bc = new BracedCode(leftBracePair, textPair, rightBracePair);
			return Tuple.of(bc.textComponent, bc.allLines);
		};
		return State.lift3(f, LinesComponent.FACTORY("{"), EScrollingSyntaxTextArea2.FACTORY(rows, cols),
				LinesComponent.FACTORY("}"));
	}

	private final Cell<Lines> allLines;
	private final Cell<Lines> leftBraceLines;
	private final Cell<Lines> rightBraceLines;
	private final EScrollingSyntaxTextArea2 textComponent;
	private final Cell<Lines> textLines;

	public BracedCode(final Tuple2<JComponent, Cell<Lines>> leftBracePair,
			final Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>> textPair,
			final Tuple2<JComponent, Cell<Lines>> rightBracePair) {
		leftBraceLines = leftBracePair._2;
		textComponent = textPair._1;
		textLines = textPair._2;
		rightBraceLines = rightBracePair._2;
		allLines = leftBraceLines.lift(textLines, rightBraceLines, Lines::combine);
	}

	@Override
	public Cell<Lines> data() {
		return allLines;
	}

	@Override
	public EScrollingSyntaxTextArea2 swingComponent() {
		return textComponent;
	}
}
