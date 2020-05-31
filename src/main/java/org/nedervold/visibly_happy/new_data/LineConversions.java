package org.nedervold.visibly_happy.new_data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LineConversions {

	public static String fromLines(final List<String> lines) {
		return lines.stream().map((s) -> s + "\n").collect(Collectors.joining());
	}

	private static String readLine(final BufferedReader br) {
		try {
			return br.readLine();
		} catch (final IOException e) {
			// Should be impossible.
			throw new RuntimeException(e);
		}
	}

	public static List<String> toLines(final String str) {
		final ArrayList<String> result = new ArrayList<>();
		final StringReader sr = new StringReader(str);
		final BufferedReader br = new BufferedReader(sr);
		String line;
		while ((line = readLine(br)) != null) {
			result.add(line);
		}
		return Collections.unmodifiableList(result);
	}

	private LineConversions() {
	}
}
