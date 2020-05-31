package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.visibly_happy.component.BracedCode;
import org.nedervold.visibly_happy.component.EScrollingSyntaxTextArea2;
import org.nedervold.visibly_happy.new_data.Lines;

import io.vavr.Tuple2;
import nz.sodium.Cell;

public class TestPane extends JFrame {
	public TestPane() {
		super("test");
		final Cell<Integer> startingLineNumber = new Cell<>(1);
		final Tuple2<Cell<Integer>, Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>>> x = BracedCode.FACTORY(25, 80)
				.runState(startingLineNumber);
		final Cell<Integer> x1 = x._1;
		final Tuple2<EScrollingSyntaxTextArea2, Cell<Lines>> x2 = x._2;
		final Container cp = getContentPane();
		final EScrollingSyntaxTextArea2 textArea = x2._1;
		textArea.setBorder(BorderFactory.createTitledBorder("Braced code"));
		cp.add(textArea, BorderLayout.CENTER);
		final Cell<Lines> x22 = x2._2;
		final Cell<String> str = x1.lift(x22, (n, ls) -> "next line: " + n + "; lines:" + ls.toSourceLines());
		final DLabel label = new DLabel(str);
		cp.add(label, BorderLayout.SOUTH);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		pack();
	}
}
