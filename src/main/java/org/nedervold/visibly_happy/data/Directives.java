package org.nedervold.visibly_happy.data;

import java.util.Optional;

public class Directives implements ToSource {

	private final Optional<Integer> expect;
	private final String source;
	private final String tokenType;

	public Directives(final String tokenType, final Optional<Integer> expect, final String source) {
		super();
		this.tokenType = tokenType;
		this.expect = expect;
		this.source = source;
	}

	@Override
	public String toSource() {
		String exp;
		if (expect.isPresent()) {
			exp = "%expect " + expect.get() + "\n";
		} else {
			exp = "";
		}
		return "%tokentype {" + tokenType + "}\n" + exp + source;
	}

}
