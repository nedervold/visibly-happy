package org.nedervold.visibly_happy;

import java.awt.Color;

import org.nedervold.nawidgets.display.DWidgetImpl;
import org.nedervold.nawidgets.editor.ETextField;

import nz.sodium.Cell;
import nz.sodium.Stream;

public abstract class EColorTextField extends ETextField {

	static class ColorImpl extends DWidgetImpl<EColorTextField, Color> {

		public ColorImpl(final EColorTextField component, final Cell<Color> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Color value) {
			component.setForeground(value);
		}
	}

	private final ColorImpl colorImpl;

	protected EColorTextField(final Stream<String> inputStream, final String initText, final int width) {
		super(inputStream, initText, width);
		final Cell<Color> colorCell = outputCell().map((str) -> isValidInput(str) ? Color.BLACK : Color.RED);
		colorImpl = new ColorImpl(this, colorCell);
	}

	// TODO This should be String -> Color. This is less an EColorTextField than an
	// EValidTextField.
	protected abstract boolean isValidInput(String input);

	@Override
	public void unlisten() {
		colorImpl.unlisten();
		super.unlisten();
	}
}
