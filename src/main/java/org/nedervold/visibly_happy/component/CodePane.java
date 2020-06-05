package org.nedervold.visibly_happy.component;

import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.editor.ECheckBox;
import org.nedervold.visibly_happy.monad.State;
import org.nedervold.visibly_happy.new_data.Lines;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.Unit;

public class CodePane extends DBox<JComponent> implements SourceComponent<CodePane, Lines> {

	private static Tuple3<Cell<List<JComponent>>, Cell<Lines>, Unit> createTuple(
			final Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>> textPair) {
		final EScrollingSyntaxTextArea2 comp = textPair._1;
		final ECheckBox checkBox = new ECheckBox("include code section", new Stream<Boolean>(), false);
		final Cell<Boolean> includeCell = checkBox.outputCell();

		final List<JComponent> shortList = Arrays.asList(checkBox);
		final List<JComponent> longList = Arrays.asList(checkBox, comp);
		final Cell<List<JComponent>> x = includeCell.map((b) -> b ? longList : shortList);

		final Box longCheckBox = Box.createHorizontalBox();
		longCheckBox.add(checkBox);
		longCheckBox.add(Box.createHorizontalGlue());

		final Cell<Lines> codeLines = textPair._2;
		final Cell<Lines> emptyLines = new Cell<>(Lines.EMPTY_LINES());
		final Cell<Cell<Lines>> lines2 = includeCell.map((b) -> b ? codeLines : emptyLines);
		final Cell<Lines> data = Cell.switchC(lines2);
		return Tuple.of(x, data, Unit.UNIT);
	}

	public static State<Cell<Integer>, Tuple2<CodePane, Cell<Lines>>> FACTORY(final int rows, final int cols) {
		final Function1<Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>>, Tuple2<CodePane, Cell<Lines>>> f = (
				textPair) -> {
			final EScrollingSyntaxTextArea2 textArea = textPair._1;
			final Cell<Lines> lines = textPair._2;
			final CodePane cp = new CodePane(Tuple.of(textArea, lines));
			return Tuple.of(cp.swingComponent(), cp.data());
		};
		/*
		 * TODO This is wrong: I'm not changing the state properly. Something like:
		 *
		 *
		 * factory :: Int -> Int -> State (Cell Int) (CodePane, Cell Lines) factory rows
		 * cols = do cb <- mkCheckBox origStartLine <- get essta2 <- mkESSTA2 rows cols
		 * nextStartLine <- get let f selected :: Bool -> Cell Int = if selected then
		 * nextStartLine else origStartLine let cc :: Cell (Cell Int) = fmap f (data cb)
		 * let c :: Cell Int = switchC cc put c pure $ CodePane cb essta2
		 */
		return BracedCode.FACTORY(rows, cols).map(f);
	}

	private final Cell<Lines> data;

	private CodePane(final Cell<List<JComponent>> inputCell, final Cell<Lines> data) {
		super(BoxLayout.Y_AXIS, inputCell);
		this.data = data;
	}

	public CodePane(final Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>> textPair) {
		this(createTuple(textPair));
	}

	private CodePane(final Tuple3<Cell<List<JComponent>>, Cell<Lines>, Unit> t) {
		this(t._1, t._2);
	}

	@Override
	public Cell<Lines> data() {
		return data;
	}

	@Override
	public CodePane swingComponent() {
		return this;
	}

}
