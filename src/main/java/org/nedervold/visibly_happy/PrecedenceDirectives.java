package org.nedervold.visibly_happy;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.nedervold.nawidgets.display.DBox;
import org.nedervold.nawidgets.editor.EComboBox;
import org.nedervold.nawidgets.editor.ETextField;

import nz.sodium.Cell;
import nz.sodium.Stream;

public class PrecedenceDirectives extends JPanel {
	public static class PrecedenceDirective extends Box {
		private final LineNumLabel lineNumLabel;

		public PrecedenceDirective(final String str) {
			super(BoxLayout.X_AXIS);
			lineNumLabel = new LineNumLabel(new Cell<>(Optional.of(2020)));

			add(lineNumLabel);
			add(Box.createHorizontalStrut(5));
			final SignButton button = new SignButton("-");
			add(button);
			add(new EComboBox<>(new Stream<>(), new String[] { "%left", "%right", "%nonassoc" }));
			add(new ETextField(new Stream<>(), str, 20));
			add(Box.createHorizontalGlue());
		}

	}

	public static class PrecedenceDirectiveList extends DBox<PrecedenceDirective> {
		private final Cell<Integer> outputLineNumber;

		public PrecedenceDirectiveList(final Cell<Integer> inputLineNumberCell) {
			super(BoxLayout.Y_AXIS,
					new Cell<>(Arrays
							.asList(new PrecedenceDirective[] { new PrecedenceDirective("<precedence directive #1>"),
									new PrecedenceDirective("<precedence directive #2>"),
									new PrecedenceDirective("<precedence directive #3>") })));
			outputLineNumber = inputLineNumberCell;
		}

		public Cell<Integer> getOutputLineNumber() {
			return outputLineNumber;
		}
	}

	private final Cell<Integer> outputLineNumber;

	public PrecedenceDirectives(final Cell<Integer> inputLineNumberCell) {
		super(new BorderLayout());
		final PrecedenceDirectiveList precList = new PrecedenceDirectiveList(inputLineNumberCell);
		final JScrollPane scroll = new JScrollPane(precList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll, BorderLayout.CENTER);

		final Box hbox = Box.createHorizontalBox();
		hbox.add(new SignButton("+"));
		hbox.add(Box.createHorizontalGlue());
		add(hbox, BorderLayout.SOUTH);
		setBorder(BorderFactory.createTitledBorder("precedences"));
		outputLineNumber = precList.getOutputLineNumber();
	}

	public Cell<Integer> getOutputLineNumber() {
		return outputLineNumber;
	}
}