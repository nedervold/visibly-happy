package org.nedervold.visibly_happy.data;

import java.util.Optional;

public class Directives implements ToSource {

	private final Optional<Integer> expect;
	private final String source;
	private final String tokenType;

	public Directives(final String tokenType, final String source, final Optional<Integer> expect) {
		super();
		this.tokenType = tokenType;
		this.source = source;
		this.expect = expect;
	}

	@Override
	public String toSource() {
		String exp;
		if (expect.isPresent()) {
			exp = "%expect " + expect.get() + "\n";
		} else {
			exp = "";
		}
		return "%tokentype {" + tokenType + "}\n" + source + exp;
	}

}
