package org.nedervold.visibly_happy;

import java.awt.Color;

import org.nedervold.nawidgets.display.DLabel;
import org.nedervold.nawidgets.display.DWidgetImpl;

import nz.sodium.Cell;

public class DColorLabel extends DLabel {

	static class ColorImpl extends DWidgetImpl<DColorLabel, Color> {

		public ColorImpl(final DColorLabel component, final Cell<Color> inputCell) {
			super(component, inputCell);
		}

		@Override
		public void setComponentValue(final Color value) {
			component.setForeground(value);
		}
	}

	protected final ColorImpl colorImpl;

	public DColorLabel(final Cell<String> inputCell, final Cell<Color> colorInputCell) {
		super(inputCell);
		colorImpl = new ColorImpl(this, colorInputCell);
	}

	@Override
	public void unlisten() {
		colorImpl.unlisten();
		super.unlisten();
	}
}
