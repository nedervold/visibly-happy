package org.nedervold.visibly_happy.monad;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Function6;
import io.vavr.Function7;
import io.vavr.Function8;

@FunctionalInterface
public interface Reader<R, A> {
	public static <A, B, R> Reader<R, B> ap(final Reader<R, Function1<A, B>> f, final Reader<R, A> a) {
		return f.bind((f2) -> a.bind((a2) -> pure(f2.apply(a2))));
	}

	public static <R> Reader<R, R> ask() {
		return (r) -> r;
	}

	public static <R, A, B, C> Reader<R, C> lift2(final Function2<A, B, C> func, final Reader<R, A> a,
			final Reader<R, B> b) {
		final Function1<A, Function1<B, C>> curried = (a2) -> (b2) -> func.apply(a2).apply(b2);
		return ap(a.map(curried), b);
	}

	public static <R, A, B, C, D> Reader<R, D> lift3(final Function3<A, B, C, D> func, final Reader<R, A> a,
			final Reader<R, B> b, final Reader<R, C> c) {
		final Function1<A, Function1<B, Function1<C, D>>> curried = (
				a2) -> (b2) -> (c2) -> func.apply(a2).apply(b2).apply(c2);
		return ap(ap(a.map(curried), b), c);
	}

	public static <R, A, B, C, D, E> Reader<R, E> lift4(final Function4<A, B, C, D, E> func, final Reader<R, A> a,
			final Reader<R, B> b, final Reader<R, C> c, final Reader<R, D> d) {
		final Function1<A, Function1<B, Function1<C, Function1<D, E>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> func.apply(a2).apply(b2).apply(c2).apply(d2);
		return ap(ap(ap(a.map(curried), b), c), d);
	}

	public static <R, A, B, C, D, E, F> Reader<R, F> lift5(final Function5<A, B, C, D, E, F> func, final Reader<R, A> a,
			final Reader<R, B> b, final Reader<R, C> c, final Reader<R, D> d, final Reader<R, E> e) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, F>>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> (e2) -> func.apply(a2).apply(b2).apply(c2).apply(d2).apply(e2);
		return ap(ap(ap(ap(a.map(curried), b), c), d), e);
	}

	public static <R, A, B, C, D, E, F, G> Reader<R, G> lift6(final Function6<A, B, C, D, E, F, G> func,
			final Reader<R, A> a, final Reader<R, B> b, final Reader<R, C> c, final Reader<R, D> d,
			final Reader<R, E> e, final Reader<R, F> f) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, G>>>>>> curried = (a2) -> (
				b2) -> (c2) -> (d2) -> (e2) -> (f2) -> func.apply(a2).apply(b2).apply(c2).apply(d2).apply(e2).apply(f2);
		return ap(ap(ap(ap(ap(a.map(curried), b), c), d), e), f);
	}

	public static <R, A, B, C, D, E, F, G, H> Reader<R, H> lift7(final Function7<A, B, C, D, E, F, G, H> func,
			final Reader<R, A> a, final Reader<R, B> b, final Reader<R, C> c, final Reader<R, D> d,
			final Reader<R, E> e, final Reader<R, F> f, final Reader<R, G> g) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, H>>>>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> (e2) -> (
						f2) -> (g2) -> func.apply(a2).apply(b2).apply(c2).apply(d2).apply(e2).apply(f2).apply(g2);
		return ap(ap(ap(ap(ap(ap(a.map(curried), b), c), d), e), f), g);
	}

	public static <R, A, B, C, D, E, F, G, H, I> Reader<R, I> lift8(final Function8<A, B, C, D, E, F, G, H, I> func,
			final Reader<R, A> a, final Reader<R, B> b, final Reader<R, C> c, final Reader<R, D> d,
			final Reader<R, E> e, final Reader<R, F> f, final Reader<R, G> g, final Reader<R, H> h) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, Function1<H, I>>>>>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> (e2) -> (f2) -> (g2) -> (h2) -> func.apply(a2).apply(b2).apply(c2)
						.apply(d2).apply(e2).apply(f2).apply(g2).apply(h2);
		return ap(ap(ap(ap(ap(ap(ap(a.map(curried), b), c), d), e), f), g), h);
	}

	public static <R, A> Reader<R, A> pure(final A a) {
		return (r) -> a;
	}

	public default <B> Reader<R, B> bind(final Function1<A, Reader<R, B>> f) {
		return (r) -> {
			final A a = runReader(r);
			final Reader<R, B> m = f.apply(a);
			return m.runReader(r);
		};
	}

	public default <B> Reader<R, B> map(final Function1<A, B> f) {
		return (r) -> f.apply(runReader(r));
	}

	public A runReader(R r);

}