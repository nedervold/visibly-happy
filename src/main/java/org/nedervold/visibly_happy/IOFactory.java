package org.nedervold.visibly_happy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import io.vavr.CheckedFunction1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.control.Try;
import nz.sodium.Stream;
import nz.sodium.StreamSink;

public class IOFactory {

	private static void deleteAll(final File entry) {
		final File[] contents = entry.listFiles();
		if (contents == null) {
			// It's a file, not a directory.
			entry.delete();
		} else {
			// It's a directory; delete the directory's contents first
			for (final File file : contents) {
				deleteAll(file);
			}
			entry.delete();
		}
	}

	private static String drainStream(final InputStream inputStream) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final InputStreamReader ir = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		final BufferedReader br = new BufferedReader(ir);
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}

	public static String findExecutable(final String name) {
		final String[] executablePaths = System.getenv("PATH").split(File.pathSeparator);
		for (final String dirname : executablePaths) {
			final File file = new File(dirname, name);
			if (file.isFile() && file.canExecute()) {
				return file.getAbsolutePath();
			}
		}
		throw new AssertionError("Found no executable " + name + " in " + String.join(", ", executablePaths) + ".");
	}

	public static <R, S> Tuple2<Runnable, Stream<Try<R>>> mapInThread(final Stream<S> inputStream,
			final CheckedFunction1<S, R> f) {
		final ExecutorService executor = Executors.newCachedThreadPool();
		final LinkedBlockingQueue<Future<Try<R>>> queue = new LinkedBlockingQueue<>();
		final Stream<Callable<Try<R>>> tasks = inputStream.map((s) -> () -> {
			return CheckedFunction1.liftTry(f).apply(s);
		});
		final Stream<Future<Try<R>>> futures = tasks.map((t) -> executor.submit(t));
		futures.listen((fut) -> queue.add(fut));

		final StreamSink<Try<R>> outputStream = new StreamSink<>();
		final Runnable pump = () -> {
			try {
				while (true) {
					final Future<Try<R>> fut = queue.take();
					Try<R> res;
					try {
						res = fut.get();
					} catch (final ExecutionException e) {
						res = Try.failure(e);
					}
					outputStream.send(res);
				}
			} catch (final InterruptedException e) {
				// Exit gracefully.
			}
		};
		return Tuple.of(pump, outputStream);
	}

	public static Tuple3<Integer, String, String> runHappy(final String stdin)
			throws IOException, InterruptedException {
		final Path tempDir = Files.createTempDirectory("visibly-happy");
		try {
			final Path happyFile = tempDir.resolve("Parser.y");
			final FileWriter fw = new FileWriter(happyFile.toFile());
			fw.write(stdin);
			fw.close();
			final ProcessBuilder builder = new ProcessBuilder().command(findExecutable("happy"), "Parser.y")
					.directory(tempDir.toFile());
			final Process process = builder.start();
			final int exitCode = process.waitFor();
			final String stderr = drainStream(process.getErrorStream());
			final String stdout = drainStream(process.getInputStream());
			return Tuple.of(exitCode, stderr, stdout);
		} finally {
			deleteAll(tempDir.toFile());
		}
	}

	private IOFactory() {

	}

}
