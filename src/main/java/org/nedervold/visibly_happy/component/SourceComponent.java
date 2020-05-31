package org.nedervold.visibly_happy.component;

import javax.swing.JComponent;

import org.nedervold.visibly_happy.new_data.ToSourceLines;

import nz.sodium.Cell;

public interface SourceComponent<C extends JComponent, D extends ToSourceLines> {
	Cell<D> data();

	C swingComponent();
}
