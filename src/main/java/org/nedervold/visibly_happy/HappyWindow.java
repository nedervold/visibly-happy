package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.nedervold.nawidgets.display.DLabel;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;
import nz.sodium.Unit;

public class HappyWindow extends JFrame {

	private static final String preSource = TextUtils.unlines(
			new String[] { "{", "module Parser where", "}", "%tokentype {()}", "%token UNIT {$$}" });
	private static final String postSource = TextUtils.unlines(new String[] { "foo : {- empty -} {()}" });

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

			final StreamSink<String> happyInputStream = new StreamSink<>();
			final EScrollingSyntaxTextArea preSourcePane = new EScrollingSyntaxTextArea(15, 80, happyInputStream,
					preSource, new Cell<>(1));
			Cell<Integer> preLineCount = preSourcePane.outputCell().map((s) -> TextUtils.countLines(TextUtils.ensureFinalNewline(s)));
			DLabel preLineCountLabel = new DLabel(preLineCount.map((n) -> "preLineCount = " + n));
			hbox.add(preLineCountLabel);
			getContentPane().add(hbox, BorderLayout.NORTH);
			final EScrollingSyntaxTextArea postSourcePane = new EScrollingSyntaxTextArea(15, 80, happyInputStream,
					postSource, preLineCount.map((n) -> n + 2));
			preSourcePane.setBorder(BorderFactory.createTitledBorder("Header and directives"));
			postSourcePane.setBorder(BorderFactory.createTitledBorder("Grammar and footer"));
			final Box vbox = Box.createVerticalBox();
			vbox.add(preSourcePane);
			vbox.add(postSourcePane);
			getContentPane().add(vbox, BorderLayout.CENTER);

			setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
			return runOutputStream.snapshot(preSourcePane.outputCell(), postSourcePane.outputCell(),
					(u, pre, post) -> new HappySource(pre, post));
		});
		setVisible(true);
	}

}
