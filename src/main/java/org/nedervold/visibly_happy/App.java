package org.nedervold.visibly_happy;

import javax.swing.SwingUtilities;

public class App {
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> {
			new HappyWindow("Happy Window");
		});
	}
}
