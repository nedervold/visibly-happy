package org.nedervold.visibly_happy.data;

import org.nedervold.visibly_happy.TextUtils;

public class RawSource implements ToSource {
	final protected String source;

	public RawSource(final String source) {
		this.source = TextUtils.ensureFinalNewline(source);
	}

	@Override
	public String toSource() {
		return source;
	}

}
