package org.nedervold.visibly_happy;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TextUtils {

	protected static String unlines(String[] lines) {
		return Arrays.asList(lines).stream().map((s) -> s + "\n").collect(Collectors.joining());
	}

	protected static String ensureFinalNewline(final String str) {
		if (str.isEmpty() || str.endsWith("\n")) {
			return str;
		} else {
			return str + "\n";
		}
	}

	private TextUtils() {
	}

	protected static int countLines(String s) {
		int res = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\n') {
				res += 1;
			}
		}
		return res;
	}

}