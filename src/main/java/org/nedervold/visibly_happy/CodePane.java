package org.nedervold.visibly_happy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.editor.ECheckBox;
import org.nedervold.nawidgets.editor.Editor;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import nz.sodium.Cell;
import nz.sodium.Stream;

public class CodePane extends DBox<JComponent> implements Editor<String> {
	private static Tuple3<ECheckBox, BracedPane, Cell<List<JComponent>>> createComponents(final int rows,
			final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		final ECheckBox checkBox = new ECheckBox("include code section", new Stream<Boolean>(), false);
		final Cell<Boolean> includeCell = checkBox.outputCell();
		final BracedPane bracedPane = new BracedPane(rows, cols, inputStream, initValue, inputLineNumberCell,
				includeCell);
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
		return Tuple.of(checkBox, bracedPane, result);
	}

	private final BracedPane bracedPane;

	private final ECheckBox checkBox;

	public CodePane(final int rows, final int cols, final Stream<String> inputStream, final String initValue,
			final Cell<Integer> inputLineNumberCell) {
		this(createComponents(rows, cols, inputStream, initValue, inputLineNumberCell));
	}

	private CodePane(final Tuple3<ECheckBox, BracedPane, Cell<List<JComponent>>> t) {
		super(BoxLayout.Y_AXIS, t._3);
		checkBox = t._1;
		bracedPane = t._2;
	}

	public Cell<Integer> lineCountCell() {
		final Cell<Boolean> includeCell = checkBox.outputCell();
		return includeCell.lift(bracedPane.lineCountCell(), (b, lc) -> b ? lc : 0);
	}

	@Override
	public Cell<String> outputCell() {
		final Cell<Boolean> includeCell = checkBox.outputCell();
		return includeCell.lift(bracedPane.outputCell(), (b, lc) -> b ? lc : "");
	}

	@Override
	public void removeNotify() {
		bracedPane.unlisten();
		checkBox.removeNotify();
		super.removeNotify();
	}
}
