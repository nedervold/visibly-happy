package org.nedervold.visibly_happy.data;

import org.nedervold.visibly_happy.TextUtils;

public class HappySource implements ToSource {

	public final Directives directives;
	public final RawSource grammar;
	public final Code header;
	public final Code trailer;

	public HappySource(final Code header, final Directives directives, final RawSource grammar, final Code trailer) {
		this.header = header;
		this.directives = directives;
		this.grammar = grammar;
		this.trailer = trailer;
	}

	@Override
	public String toSource() {
		return header.toSource() + directives.toSource() + TextUtils.PERCENTS + grammar.toSource() + trailer.toSource();
	}

}
