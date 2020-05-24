package org.nedervold.visibly_happy;

public class HappySource {

	public final String directives;
	public final String grammar;
	public final String header;
	public final String trailer;

	public HappySource(final String header, final String directives, final String grammar, final String trailer) {
		this.header = header;
		this.directives = directives;
		this.grammar = grammar;
		this.trailer = trailer;
	}

	public String getSource() {
		return header + TextUtils.ensureFinalNewline(directives) + TextUtils.PERCENTS
				+ TextUtils.ensureFinalNewline(grammar) + trailer;
	}

}
