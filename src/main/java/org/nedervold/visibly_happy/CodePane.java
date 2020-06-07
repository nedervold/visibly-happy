package org.nedervold.visibly_happy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.editor.ECheckBox;
import org.nedervold.nawidgets.editor.Editor;
import org.nedervold.visibly_happy.data.Code;

import io.vavr.Tuple;
import io.vavr.Tuple4;
import nz.sodium.Cell;
import nz.sodium.Stream;

public class CodePane extends DBox<JComponent> implements Editor<Code>, Style {
	private static Tuple4<Cell<Integer>, ECheckBox, BracedPane, Cell<List<JComponent>>> createComponents(final int rows,
			final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		final ECheckBox checkBox = new ECheckBox("include code section", new Stream<Boolean>(), false);
		final Cell<Boolean> includeCell = checkBox.outputCell();
		final BracedPane bracedPane = new BracedPane(rows, cols, inputStream, initValue, inputLineNumberCell);
		final Box longCheckBox = Box.createHorizontalBox();
		longCheckBox.add(checkBox);
		longCheckBox.add(Box.createHorizontalGlue());

		final Cell<List<JComponent>> result = includeCell.map((b) -> {
			final List<JComponent> list = new ArrayList<>();
			list.add(longCheckBox);
			if (b) {
				list.add(bracedPane);
			}
			return list;
		});
		return Tuple.of(inputLineNumberCell, checkBox, bracedPane, result);
	}

	private final BracedPane bracedPane;
	private final ECheckBox checkBox;
	private final Cell<Integer> outputLineNumber;

	public CodePane(final Stream<String> inputStream, final String initValue, final Cell<Integer> inputLineNumberCell) {
		this(createComponents(ROWS, COLS, inputStream, initValue, inputLineNumberCell));
	}

	private CodePane(final Tuple4<Cell<Integer>, ECheckBox, BracedPane, Cell<List<JComponent>>> t) {
		super(BoxLayout.Y_AXIS, t._4);
		final Cell<Integer> inputLineNumberCell = t._1;
		checkBox = t._2;
		bracedPane = t._3;
		final Cell<Cell<Integer>> outputLineNumberCell = checkBox.outputCell()
				.map((cb) -> cb ? bracedPane.getOutputLineNumber() : inputLineNumberCell);
		outputLineNumber = Cell.switchC(outputLineNumberCell);
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}

	@Override
	public Cell<Code> outputCell() {
		final Cell<Boolean> includeCell = checkBox.outputCell();
		return includeCell.lift(bracedPane.outputCell(), (b, lc) -> b ? lc : "").map(Code::new);
	}

	@Override
	public void unlisten() {
		bracedPane.unlisten();
		checkBox.unlisten();
		super.unlisten();
	}
}
