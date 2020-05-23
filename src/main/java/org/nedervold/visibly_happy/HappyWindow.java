package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.fife.ui.rtextarea.RTextScrollPane;

import nz.sodium.Stream;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;
import nz.sodium.Unit;

public class HappyWindow extends JFrame {

	private final String[] initSourceLines = new String[] { "%tokentype {()}", "%token UNIT {$$}", "%%",
			"foo : {- empty -} {()}" };

	private final String initSourceX = Arrays.asList(initSourceLines).stream().map((s) -> s + "\n")
			.collect(Collectors.joining());

	public final Stream<String> outputStream;

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

			final StreamSink<String> happyInputStream = new StreamSink<>();
			final ESyntaxTextArea sourcePane = new ESyntaxTextArea(30, 80, happyInputStream,
					"-- Type Happy code here.\n\n" + initSourceX);
			final RTextScrollPane scroll = new RTextScrollPane(sourcePane);
			scroll.setLineNumbersEnabled(true);
			scroll.setBorder(BorderFactory.createTitledBorder("Happy source"));
			getContentPane().add(scroll, BorderLayout.CENTER);

			setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
			return runOutputStream.snapshot(sourcePane.outputCell());
		});
		setVisible(true);
	}

}
