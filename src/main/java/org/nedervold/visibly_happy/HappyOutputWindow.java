package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.nedervold.nawidgets.display.DTextArea;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.Transaction;

public class HappyOutputWindow extends JFrame {

	private final DColorLabel exitCodeLabel;
	private final DTextArea stderrPane;
	private final DTextArea stdoutPane;

	public HappyOutputWindow(final String title, final Stream<Tuple3<Integer, String, String>> processResult)
			throws HeadlessException {
		super(title);
		final Container cp = getContentPane();
		final Tuple3<DTextArea, DTextArea, DColorLabel> fields = Transaction.run(() -> {
			final Cell<Color> colorCell = processResult
					.map((t) -> (t._1 == 0 ? Color.green.darker().darker() : Color.red)).hold(Color.black);
			final Stream<String> exitCodeStream = processResult.map((t) -> {
				final int exitCode = t._1;
				if (exitCode == 0) {
					return "Success.";
				} else {
					return "Failure: exit code = " + exitCode + ".";
				}
			});
			final Cell<String> exitCodeCell = exitCodeStream.hold("No process.");
			final DColorLabel exitCodeLabel = new DColorLabel(exitCodeCell, colorCell);
			exitCodeLabel.setFont(exitCodeLabel.getFont().deriveFont(Font.BOLD, 16.0f));
			final int S = 8;
			exitCodeLabel.setBorder(BorderFactory.createEmptyBorder(S, S, S, S));
			cp.add(exitCodeLabel, BorderLayout.NORTH);

			final DTextArea stderrPane = new DTextArea(10, 40, processResult.map(Tuple3::_2).hold(""));
			final JScrollPane errScroll = new JScrollPane(stderrPane);
			errScroll.setBorder(BorderFactory.createTitledBorder("standard error"));

			final DTextArea stdoutPane = new DTextArea(10, 40, processResult.map(Tuple3::_3).hold(""));
			final JScrollPane outScroll = new JScrollPane(stdoutPane);
			outScroll.setBorder(BorderFactory.createTitledBorder("standard output"));

			final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, errScroll, outScroll);
			splitPane.setDividerLocation(0.5);
			cp.add(splitPane, BorderLayout.CENTER);
			pack();
			return Tuple.of(stderrPane, stdoutPane, exitCodeLabel);
		});
		stderrPane = fields._1;
		stdoutPane = fields._2;
		exitCodeLabel = fields._3;
	}

	@Override
	public void removeNotify() {
		unlisten();
		super.removeNotify();
	}

	public void unlisten() {
		exitCodeLabel.unlisten();
		stderrPane.unlisten();
		stdoutPane.unlisten();
	}
}
