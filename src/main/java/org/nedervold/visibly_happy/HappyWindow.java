package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.nedervold.visibly_happy.data.HappySource;
import org.nedervold.visibly_happy.data.RawSource;

import io.vavr.Tuple;
import io.vavr.Tuple6;
import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;
import nz.sodium.Unit;

public class HappyWindow extends JFrame {

	private static final int COLS = 80;
	private static final String DIRECTIVES_SOURCE = TextUtils.unlines(new String[] { "%token UNIT {$$}" });
	private static final String GRAMMAR_SOURCE = TextUtils.unlines(new String[] { "foo : {- empty -} {()}" });
	private static final String HEADER_SOURCE = TextUtils.unlines(new String[] { "module Parser where" });
	private static final StreamSink<String> NEVER = new StreamSink<>();
	private static final int ROWS = 12;
	private static final int STARTING_LINE_NUM = 1;

	private static final String TRAILER_SOURCE = "";
	private final DirectivesPane directivesPane;
	private final GrammarPane grammarPane;
	public final Cell<HappySource> happySourceCell;
	private final HeaderPane headerPane;
	public final Stream<HappySource> runOutputStream;
	private final TrailerPane trailerPane;

	public HappyWindow(final String title) throws HeadlessException {
		super(title);
		final Tuple6<Cell<HappySource>, Stream<HappySource>, HeaderPane, DirectivesPane, GrammarPane, TrailerPane> t = Transaction
				.run(() -> {
					final Container contentPane = getContentPane();
					final Box contents = Box.createVerticalBox();

					final JButton runButton = new JButton("Run…");
					final StreamSink<Unit> runOutputStream = new StreamSink<>();
					runButton.addActionListener((e) -> {
						runOutputStream.send(Unit.UNIT);
					});
					final Box hbox = Box.createHorizontalBox();
					hbox.add(runButton);
					hbox.add(Box.createHorizontalGlue());

					final HeaderPane headerPane = new HeaderPane(ROWS, COLS, NEVER, HEADER_SOURCE,
							new Cell<>(STARTING_LINE_NUM));

					final Cell<Integer> directivesStartingCount = headerPane.getOutputLineNumber();
					final DirectivesPane directivesPane = new DirectivesPane(ROWS, COLS, NEVER, DIRECTIVES_SOURCE,
							directivesStartingCount);

					final Cell<Integer> percentStartingCount = directivesPane.getOutputLineNumber();
					final Cell<Integer> grammarStartingCount = percentStartingCount
							.map((n) -> n + RawSource.PERCENT_SRC.toLineCount());
					final GrammarPane grammarPane = new GrammarPane(ROWS, COLS, NEVER, GRAMMAR_SOURCE,
							grammarStartingCount);

					final Cell<Integer> trailerStartingCount = grammarPane.getOutputLineNumber();
					final TrailerPane trailerPane = new TrailerPane(ROWS, COLS, NEVER, TRAILER_SOURCE,
							trailerStartingCount);

					final Box paneBox = Box.createVerticalBox();
					paneBox.add(headerPane);
					paneBox.add(directivesPane);
					paneBox.add(grammarPane);
					paneBox.add(trailerPane);
					contents.add(paneBox);
					contentPane.add(hbox, BorderLayout.NORTH);
					contentPane.add(new JScrollPane(contents), BorderLayout.CENTER);

					setDefaultCloseOperation(EXIT_ON_CLOSE);
					pack();
					final Cell<HappySource> happySourceCell = headerPane.outputCell().lift(directivesPane.outputCell(),
							grammarPane.outputCell(), trailerPane.outputCell(),
							(header, dirs, grammar, trailer) -> new HappySource(header, dirs, grammar, trailer));
					final Stream<HappySource> runHappySourceStream = runOutputStream.snapshot(happySourceCell,
							(u, happySource) -> happySource);
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
