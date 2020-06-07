package org.nedervold.visibly_happy.data;

import org.nedervold.visibly_happy.TextUtils;

public class Code extends RawSource {

	public Code(final String source) {
		super(source);
	}

	@Override
	public String toSource() {
		return TextUtils.wrapInBraces(source);
	}

}
