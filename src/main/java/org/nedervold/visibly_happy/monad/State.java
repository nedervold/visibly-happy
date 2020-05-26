package org.nedervold.visibly_happy.monad;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Function6;
import io.vavr.Function7;
import io.vavr.Function8;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import nz.sodium.Unit;

@FunctionalInterface
public interface State<S, A> {
	public static <A, B, S> State<S, B> ap(final State<S, Function1<A, B>> f, final State<S, A> a) {
		return f.bind((f2) -> a.bind((a2) -> pure(f2.apply(a2))));
	}

	public static <S> State<S, S> get() {
		return (s) -> Tuple.of(s, s);
	}

	public static <S, A, B, C> State<S, C> lift2(final Function2<A, B, C> func, final State<S, A> a,
			final State<S, B> b) {
		final Function1<A, Function1<B, C>> curried = (a2) -> (b2) -> func.apply(a2).apply(b2);
		return ap(a.map(curried), b);
	}

	public static <S, A, B, C, D> State<S, D> lift3(final Function3<A, B, C, D> func, final State<S, A> a,
			final State<S, B> b, final State<S, C> c) {
		final Function1<A, Function1<B, Function1<C, D>>> curried = (
				a2) -> (b2) -> (c2) -> func.apply(a2).apply(b2).apply(c2);
		return ap(ap(a.map(curried), b), c);
	}

	public static <S, A, B, C, D, E> State<S, E> lift4(final Function4<A, B, C, D, E> func, final State<S, A> a,
			final State<S, B> b, final State<S, C> c, final State<S, D> d) {
		final Function1<A, Function1<B, Function1<C, Function1<D, E>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> func.apply(a2).apply(b2).apply(c2).apply(d2);
		return ap(ap(ap(a.map(curried), b), c), d);
	}

	public static <S, A, B, C, D, E, F> State<S, F> lift5(final Function5<A, B, C, D, E, F> func, final State<S, A> a,
			final State<S, B> b, final State<S, C> c, final State<S, D> d, final State<S, E> e) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, F>>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> (e2) -> func.apply(a2).apply(b2).apply(c2).apply(d2).apply(e2);
		return ap(ap(ap(ap(a.map(curried), b), c), d), e);
	}

	public static <S, A, B, C, D, E, F, G> State<S, G> lift6(final Function6<A, B, C, D, E, F, G> func,
			final State<S, A> a, final State<S, B> b, final State<S, C> c, final State<S, D> d, final State<S, E> e,
			final State<S, F> f) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, G>>>>>> curried = (a2) -> (
				b2) -> (c2) -> (d2) -> (e2) -> (f2) -> func.apply(a2).apply(b2).apply(c2).apply(d2).apply(e2).apply(f2);
		return ap(ap(ap(ap(ap(a.map(curried), b), c), d), e), f);
	}

	public static <S, A, B, C, D, E, F, G, H> State<S, H> lift7(final Function7<A, B, C, D, E, F, G, H> func,
			final State<S, A> a, final State<S, B> b, final State<S, C> c, final State<S, D> d, final State<S, E> e,
			final State<S, F> f, final State<S, G> g) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, H>>>>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> (e2) -> (
						f2) -> (g2) -> func.apply(a2).apply(b2).apply(c2).apply(d2).apply(e2).apply(f2).apply(g2);
		return ap(ap(ap(ap(ap(ap(a.map(curried), b), c), d), e), f), g);
	}

	public static <S, A, B, C, D, E, F, G, H, I> State<S, I> lift8(final Function8<A, B, C, D, E, F, G, H, I> func,
			final State<S, A> a, final State<S, B> b, final State<S, C> c, final State<S, D> d, final State<S, E> e,
			final State<S, F> f, final State<S, G> g, final State<S, H> h) {
		final Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, Function1<H, I>>>>>>>> curried = (
				a2) -> (b2) -> (c2) -> (d2) -> (e2) -> (f2) -> (g2) -> (h2) -> func.apply(a2).apply(b2).apply(c2)
						.apply(d2).apply(e2).apply(f2).apply(g2).apply(h2);
		return ap(ap(ap(ap(ap(ap(ap(a.map(curried), b), c), d), e), f), g), h);
	}

	public static <S, A> State<S, A> pure(final A a) {
		return (s) -> Tuple.of(s, a);
	}

	public static <S> State<S, Unit> put() {
		return (s) -> Tuple.of(s, Unit.UNIT);
	}

	public default <B> State<S, B> bind(final Function1<A, State<S, B>> f) {
		return (s0) -> {
			final Tuple2<S, A> t = runState(s0);
			return f.apply(t._2).runState(t._1);
		};
	}

	public default <B> State<S, B> map(final Function1<A, B> f) {
		return bind((a2) -> pure(f.apply(a2)));
	}

	public Tuple2<S, A> runState(S state);

}
