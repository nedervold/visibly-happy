package org.nedervold.visibly_happy;

import java.awt.Dimension;

import javax.swing.JButton;

public class SignButton extends JButton implements Style {

	public SignButton(final String label) {
		super(label);
		setPreferredSize(new Dimension(MAC_OS_MIN_BUTTON_WIDTH, getPreferredSize().height));
	}

}
