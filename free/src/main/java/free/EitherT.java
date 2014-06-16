package free;

public final class EitherT<F, A, B> implements _1<EitherT<F, A, ?>, B> {
  public final _1<F, Either<A, B>> run;

  public EitherT(_1<F, Either<A, B>> run) {
    this.run = run;
  }

  public <C> EitherT<F, A, C> map(final F1<B, C> f, final Functor<F> F){
    return new EitherT<>(F.map(e -> e.map(f), run));
  }

  public <C> EitherT<F, A, C> flatMap(final F1<B, EitherT<F, A, C>> f, final Monad<F> F){
    return new EitherT<>(
      F.flatMap(e -> e.fold(
        l -> F.point(() -> Either.left(l)), r -> f.apply(r).run
      ), run)
    );
  }

  public <C, D> EitherT<F, C, D> bimap(final F1<A, C> f, final F1<B, D> g, final Functor<F> F){
    return new EitherT<>(F.map(e -> e.bimap(f, g), run));
  }

  public EitherT<F, B, A> swap(final Functor<F> F){
    return new EitherT<>(F.map(Either::swap, run));
  }

  public static <F, A, B> EitherT<F, A, B> point(final F0<B> value, final Applicative<F> F){
    return new EitherT<>(F.point(() -> Either.right(value.apply())));
  }

  public static <F, L> Functor<EitherT<F, L, ?>> monad(final Functor<F> F){
    return new Functor<EitherT<F, L, ?>>() {
      @Override
      public <A, B> _1<EitherT<F, L, ?>, B> map(F1<A, B> f, _1<EitherT<F, L, ?>, A> fa) {
        return ((EitherT<F, L, A>) fa).map(f, F);
      }
    };
  }

  public static <F, L> Monad<EitherT<F, L, ?>> monad(final Monad<F> F){
    return new Monad.WithDefault<EitherT<F, L, ?>>() {
      @Override
      public <A> _1<EitherT<F, L, ?>, A> point(F0<A> a) {
        return EitherT.point(a, F);
      }

      @Override
      public <A, B> _1<EitherT<F, L, ?>, B> flatMap(F1<A, _1<EitherT<F, L, ?>, B>> f, _1<EitherT<F, L, ?>, A> fa) {
        return ((EitherT<F, L, A>)fa).flatMap(e -> (EitherT<F, L, B>)f.apply(e), F);
      }
    };
  }

}
