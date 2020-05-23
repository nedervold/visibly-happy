package org.nedervold.visibly_happy;

import javax.swing.SwingUtilities;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.control.Try;
import nz.sodium.Stream;

public class App {
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> {
			final HappyWindow happyWindow = new HappyWindow("Happy Window");
			final Tuple2<Runnable, Stream<Try<Tuple3<Integer, String, String>>>> x = IOFactory
					.mapInThread(happyWindow.outputStream.map(HappySource::getSource), IOFactory::runHappy);
			// TODO How can we plan to interrupt the thread?
			new Thread(x._1).start();
			final HappyOutputWindow outputWindow = new HappyOutputWindow("Output", x._2.map((t) -> {
				if (t.isFailure()) {
					return Tuple.of(-1, t.getCause().toString(), "");
				} else {
					return t.get();
				}
			}));
			happyWindow.outputStream.listen((src) -> {
				outputWindow.setVisible(true);
			});
		});
	}
}
