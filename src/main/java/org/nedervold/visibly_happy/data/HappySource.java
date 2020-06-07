package org.nedervold.visibly_happy.data;

public class HappySource implements ToSource {

	private final Directives directives;
	private final RawSource grammar;
	private final Code header;
	private final Code trailer;

	public HappySource(final Code header, final Directives directives, final RawSource grammar, final Code trailer) {
		this.header = header;
		this.directives = directives;
		this.grammar = grammar;
		this.trailer = trailer;
	}

	@Override
	public String toSource() {
		return header.toSource() + directives.toSource() + RawSource.PERCENT_SRC.toSource() + grammar.toSource()
				+ trailer.toSource();
	}

}
