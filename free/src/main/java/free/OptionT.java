package free;

public final class OptionT<F, A> implements _1<OptionT<F, ?>, A> {
  public final _1<F, Option<A>> run;

  public OptionT(_1<F, Option<A>> run) {
    this.run = run;
  }

  public <B> OptionT<F, B> map(final F1<A, B> f, final Functor<F> F){
    return new OptionT<>(F.map(a -> a.map(f), run));
  }

  public <B> OptionT<F, B> flatMap(final F1<A, OptionT<F, B>> f, final Monad<F> F){
    return new OptionT<>(F.flatMap(o ->
      o.<_1<F, Option<B>>>fold(
        z -> f.apply(z).run,
        () -> F.point(() -> Option.none())
      )
    , run));
  }

  public OptionT<F, A> orElse(final F0<OptionT<F, A>> a, final Monad<F> F){
    return new OptionT<>(F.flatMap(o ->
      o.isEmpty() ? a.apply().run : F.point(() -> o)
    , run));
  }

  public <B> EitherT<F, B, A> toRight(final F0<B> left, final Functor<F> F){
    return new EitherT<>(F.map(a -> a.toRight(left), run));
  }

  public <B> EitherT<F, A, B> toLeft(final F0<B> right, final Functor<F> F){
    return new EitherT<>(F.map(a -> a.toLeft(right), run));
  }

  public static <F> Functor<OptionT<F, ?>> functor(final Functor<F> F){
    return new Functor<OptionT<F, ?>>() {
      @Override
      public <A, B> _1<OptionT<F, ?>, B> map(F1<A, B> f, _1<OptionT<F, ?>, A> fa) {
        return ((OptionT<F, A>)fa).map(f, F);
      }
    };
  }

  public static <F> MonadPlus<OptionT<F, ?>> monadPlus(final Monad<F> F){
    return new MonadPlus.WithDefault<OptionT<F, ?>>() {
      @Override
      public <A> _1<OptionT<F, ?>, A> point(F0<A> a) {
        return new OptionT<>(F.point(() -> Option.some(a.apply())));
      }

      @Override
      public <A, B> _1<OptionT<F, ?>, B> flatMap(F1<A, _1<OptionT<F, ?>, B>> f, _1<OptionT<F, ?>, A> fa) {
        return ((OptionT<F, A>)fa).flatMap(o -> (OptionT<F, B>)f.apply(o), F);
      }

      @Override
      public <A> _1<OptionT<F, ?>, A> empty() {
        return new OptionT<>(F.point(() -> Option.none()));
      }

      @Override
      public <A> _1<OptionT<F, ?>, A> plus(_1<OptionT<F, ?>, A> a1, _1<OptionT<F, ?>, A> a2) {
        return ((OptionT<F, A>)a1).orElse(() -> (OptionT<F, A>)a2, F);
      }
    };
  }
}