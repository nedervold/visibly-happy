package org.nedervold.visibly_happy.data;

import org.nedervold.visibly_happy.TextUtils;

public interface ToSource {
	default int toLineCount() {
		return TextUtils.countLines(toSource());
	}

	String toSource();
}
