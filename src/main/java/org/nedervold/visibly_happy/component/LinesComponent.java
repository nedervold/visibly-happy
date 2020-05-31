package org.nedervold.visibly_happy.component;

import java.util.List;

import javax.swing.JComponent;

import org.nedervold.visibly_happy.monad.State;
import org.nedervold.visibly_happy.new_data.LineConversions;
import org.nedervold.visibly_happy.new_data.Lines;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import nz.sodium.Cell;

public class LinesComponent implements SourceComponent<JComponent, Lines> {
	public static State<Cell<Integer>, Tuple2<JComponent, Cell<Lines>>> FACTORY(final List<String> lines) {
		return (beforeLineCount) -> {
			final LinesComponent lc = new LinesComponent(lines);
			final int count = lines.size();
			final Cell<Integer> afterLineCount = beforeLineCount.map((n) -> n + count);

			return Tuple.of(afterLineCount, Tuple.of(null, lc.data()));
		};
	}

	public static State<Cell<Integer>, Tuple2<JComponent, Cell<Lines>>> FACTORY(final String lines) {
		return FACTORY(LineConversions.toLines(lines));
	}

	private final Lines lines;

	public LinesComponent(final List<String> lines) {
		this.lines = new Lines(lines);
	}

	public LinesComponent(final String lines) {
		this.lines = new Lines(lines);
	}

	@Override
	public Cell<Lines> data() {
		// TODO Optimize this;
		return new Cell<>(lines);
	}

	@Override
	public JComponent swingComponent() {
		return null;
	}
}
