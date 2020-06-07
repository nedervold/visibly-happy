package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.nedervold.nawidgets.Utils;
import org.nedervold.visibly_happy.data.HappySource;
import org.nedervold.visibly_happy.data.RawSource;

import io.vavr.Tuple;
import io.vavr.Tuple6;
import nz.sodium.Cell;
import nz.sodium.Operational;
import nz.sodium.Stream;
import nz.sodium.Transaction;
import nz.sodium.time.MillisecondsTimerSystem;

public class HappyWindow extends JFrame implements Style {

	public static class PercentBox {
		private final Cell<Integer> outputLineNumber;

		public PercentBox(final Cell<Integer> inputLineNumberCell) {
			outputLineNumber = inputLineNumberCell.map((n) -> n + RawSource.PERCENT_SRC.toLineCount());
		}

		public Cell<Integer> getOutputLineNumber() {
			return outputLineNumber;
		}
	}

	private static final long DELAY_MILLISECS = 1 * 1000L;
	private static final int STARTING_LINE_NUM = 1;
	private final DirectivesPane directivesPane;
	private final GrammarPane grammarPane;
	public final Cell<HappySource> happySourceCell;
	private final HeaderPane headerPane;
	public final Stream<HappySource> runOutputStream;

	private final TrailerPane trailerPane;

	public HappyWindow(final String title) throws HeadlessException {
		super(title);
		final MillisecondsTimerSystem sys = new MillisecondsTimerSystem();
		final Tuple6<Cell<HappySource>, Stream<HappySource>, HeaderPane, DirectivesPane, GrammarPane, TrailerPane> t = Transaction
				.run(() -> {
					final Cell<Integer> inputLineNumberCell = new Cell<>(STARTING_LINE_NUM);
					final HeaderPane headerPane = new HeaderPane(inputLineNumberCell);
					final DirectivesPane directivesPane = new DirectivesPane(headerPane.getOutputLineNumber());
					final PercentBox percentBox = new PercentBox(directivesPane.getOutputLineNumber());
					final GrammarPane grammarPane = new GrammarPane(percentBox.getOutputLineNumber());
					final TrailerPane trailerPane = new TrailerPane(grammarPane.getOutputLineNumber());

					final Container contentPane = getContentPane();
					final Box paneBox = Box.createVerticalBox();
					paneBox.add(headerPane);
					paneBox.add(directivesPane);
					// Ignore percentBox.
					paneBox.add(grammarPane);
					paneBox.add(trailerPane);
					paneBox.add(Box.createVerticalGlue());
					contentPane.add(new JScrollPane(paneBox), BorderLayout.CENTER);

					setDefaultCloseOperation(EXIT_ON_CLOSE);
					pack();
					final Cell<HappySource> happySourceCell = headerPane.outputCell().lift(directivesPane.outputCell(),
							grammarPane.outputCell(), trailerPane.outputCell(),
							(header, dirs, grammar, trailer) -> new HappySource(header, dirs, grammar, trailer));
					final Stream<HappySource> runHappySourceStream = Utils.debounce(sys, DELAY_MILLISECS,
							Operational.updates(happySourceCell));
					return Tuple.of(happySourceCell, runHappySourceStream, headerPane, directivesPane, grammarPane,
							trailerPane);
				});
		happySourceCell = t._1;
		runOutputStream = t._2;
		headerPane = t._3;
		directivesPane = t._4;
		grammarPane = t._5;
		trailerPane = t._6;
		setVisible(true);
	}

	@Override
	public void removeNotify() {
		unlisten();
		super.removeNotify();
	}

	public void unlisten() {
		headerPane.unlisten();
		directivesPane.unlisten();
		grammarPane.unlisten();
		trailerPane.unlisten();
	}
}
