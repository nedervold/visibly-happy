package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;
import nz.sodium.Unit;

public class HappyWindow extends JFrame {

	private static final int COLS = 80;
	private static final String DIRECTIVES_SOURCE = TextUtils
			.unlines(new String[] { "%tokentype {()}", "%token UNIT {$$}" });
	private static final String GRAMMAR_SOURCE = TextUtils.unlines(new String[] { "foo : {- empty -} {()}" });
	private static final String HEADER_SOURCE = TextUtils.unlines(new String[] { "module Parser where" });
	private static final StreamSink<String> NEVER = new StreamSink<>();
	private static final int ROWS = 15;
	private static final int STARTING_LINE_NUM = 1;

	private static final String TRAILER_SOURCE = "";
	public final Stream<HappySource> outputStream;

	public HappyWindow(final String title) throws HeadlessException {
		super(title);
		outputStream = Transaction.run(() -> {
			final JButton runButton = new JButton("Run…");
			final StreamSink<Unit> runOutputStream = new StreamSink<>();
			runButton.addActionListener((e) -> {
				runOutputStream.send(Unit.UNIT);
			});
			final Box hbox = Box.createHorizontalBox();
			hbox.add(runButton);
			getContentPane().add(hbox, BorderLayout.NORTH);

			final HeaderPane headerPane = new HeaderPane(ROWS, COLS, NEVER, HEADER_SOURCE,
					new Cell<>(STARTING_LINE_NUM));

			final Cell<Integer> directivesStartingCount = headerPane.lineCountCell().map((n) -> n + STARTING_LINE_NUM);

			final DirectivesPane directivesPane = new DirectivesPane(ROWS, COLS, NEVER, DIRECTIVES_SOURCE,
					directivesStartingCount);

			final Cell<Integer> grammarStartingCount = directivesStartingCount.lift(directivesPane.lineCountCell(),
					(m, n) -> m + n + TextUtils.PERCENTS_LINES);

			final GrammarPane grammarPane = new GrammarPane(ROWS, COLS, NEVER, GRAMMAR_SOURCE, grammarStartingCount);
			final Cell<Integer> trailerStartingCount = grammarStartingCount.lift(grammarPane.lineCountCell(),
					(m, n) -> m + n);
			final TrailerPane trailerPane = new TrailerPane(ROWS, COLS, NEVER, TRAILER_SOURCE, trailerStartingCount);

			final Box vbox = Box.createVerticalBox();
			vbox.add(headerPane);
			vbox.add(directivesPane);
			vbox.add(grammarPane);
			vbox.add(trailerPane);
			getContentPane().add(vbox, BorderLayout.CENTER);

			setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
			return runOutputStream.snapshot(headerPane.outputCell(), directivesPane.outputCell(),
					grammarPane.outputCell(), trailerPane.outputCell(),
					(u, header, dirs, grammar, trailer) -> new HappySource(header, dirs, grammar, trailer));
		});
		setVisible(true);
	}

}
