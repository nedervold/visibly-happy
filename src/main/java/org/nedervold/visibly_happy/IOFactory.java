package org.nedervold.visibly_happy;

import java.util.Random;
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

	public static Tuple3<Integer, String, String> runHappy(String stdin) {
		Random random = new Random();
		boolean failed = random.nextBoolean();
		int exitCode;
		if (failed) {
			exitCode = random.nextInt(254) + 1;
		} else
			exitCode = 0;

		return Tuple.of(exitCode, "stderr", stdin);
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

	private IOFactory() {

	}

}
