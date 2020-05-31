package org.nedervold.visibly_happy.new_data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lines implements ToSourceLines {

	public static Lines combine(final Lines... lotsaLines) {
		final ArrayList<String> result = new ArrayList<>();
		for (final Lines lines : lotsaLines) {
			result.addAll(lines.lines);
		}
		return new Lines(result);
	}

	private static List<String> dropTrailingBlankLines(final List<String> lines) {
		final ArrayList<String> newLines = new ArrayList<>(lines);
		while (newLines.size() > 0 && newLines.get(newLines.size() - 1).isEmpty()) {
			newLines.remove(newLines.size() - 1);
		}
		return newLines;
	}

	private final List<String> lines;

	public Lines(final List<String> lines) {
		this.lines = Collections.unmodifiableList(dropTrailingBlankLines(lines));
	}

	public Lines(final String lines) {
		this(LineConversions.toLines(lines));
	}

	public int size() {
		return lines.size();
	}

	@Override
	public List<String> toSourceLines() {
		return lines;
	}
}
