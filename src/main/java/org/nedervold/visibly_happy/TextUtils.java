package org.nedervold.visibly_happy;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TextUtils {

	static final String PERCENTS = "\n%%\n\n";

	static final int PERCENTS_LINES = countLines(ensureFinalNewline(PERCENTS));

	protected static int countLines(final String s) {
		int res = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\n') {
				res += 1;
			}
		}
		return res;
	}

	protected static String ensureFinalNewline(final String str) {
		if (str.isEmpty() || str.endsWith("\n")) {
			return str;
		} else {
			return str + "\n";
		}
	}

	protected static String unlines(final String[] lines) {
		return Arrays.asList(lines).stream().map((s) -> s + "\n").collect(Collectors.joining());
	}

	protected static String wrapInBraces(final String s) {
		if (s.isEmpty()) {
			return s;
		} else {
			return "{\n" + ensureFinalNewline(s) + "}\n";
		}
	}

	private TextUtils() {
	}

}