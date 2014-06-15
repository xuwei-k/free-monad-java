package free;

public final class Kleisli<F, A, B> implements _1<Kleisli<F, A, ?>, B>{
  public final F1<A, _1<F, B>> run;

  public Kleisli(F1<A, _1<F, B>> run) {
    this.run = run;
  }

  public <C> Kleisli<F, A, C> map(final F1<B, C> f, final Functor<F> F){
    return new Kleisli<>(run.andThen(a -> F.map(f, a)));
  }

  public <C> Kleisli<F, A, C> ap(final F0<Kleisli<F, A, F1<B, C>>> f, final Apply<F> F){
    return new Kleisli<>(a -> F.ap(() -> f.apply().run.apply(a), () -> run.apply(a)));
  }

  @SuppressWarnings("unchecked")
  public <C> Kleisli<F, A, C> flatMap(final F1<B, Kleisli<F, A, C>> f, final Bind<F> F){
    return new Kleisli<>(a -> F.flatMap(b -> (_1<F, C>) f.apply(b), run.apply(a)));
  }

  public <C> Kleisli<F, A, C> andThen(final Kleisli<F, B, C> k, final Bind<F> F){
    return new Kleisli<>(a -> F.flatMap(k.run, run.apply(a)));
  }

  public <C> Kleisli<F, C, B> compose(final Kleisli<F, C, A> k, final Bind<F> F){
    return k.andThen(this, F);
  }

  public static <F, A, B> Kleisli<F, A, B> point(final F0<B> b, final Applicative<F> F){
    return new Kleisli<>(a -> F.point(b));
  }

  public static <F, X> Functor<Kleisli<F, X, ?>> functor(final Functor<F> F){
    return new Functor<Kleisli<F, X, ?>>() {
      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> map(F1<A, B> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).map(f, F);
      }
    };
  }

  public static <F, X> Apply<Kleisli<F, X, ?>> apply(final Apply<F> F){
    return new Apply<Kleisli<F, X, ?>>() {
      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> ap(F0<_1<Kleisli<F, X, ?>, F1<A, B>>> f, F0<_1<Kleisli<F, X, ?>, A>> fa) {
        return ((Kleisli<F, X, A>)fa.apply()).ap(f.map(k -> (Kleisli<F, X, F1<A, B>>)k), F);
      }

      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> map(F1<A, B> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).map(f, F);
      }
    };
  }

  public static <F, X> Applicative<Kleisli<F, X, ?>> applicative(final Applicative<F> F){
    return new Applicative<Kleisli<F, X, ?>>() {
      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> ap(F0<_1<Kleisli<F, X, ?>, F1<A, B>>> f, F0<_1<Kleisli<F, X, ?>, A>> fa) {
        return ((Kleisli<F, X, A>)fa.apply()).ap(f.map(k -> (Kleisli<F, X, F1<A, B>>)k), F);
      }

      @Override
      public <A> _1<Kleisli<F, X, ?>, A> point(F0<A> a) {
        return Kleisli.point(a, F);
      }

      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> map(F1<A, B> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).map(f, F);
      }
    };
  }

  public static <F, X> Bind<Kleisli<F, X, ?>> bind(final Bind<F> F){
    return new Bind<Kleisli<F, X, ?>>() {
      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> map(F1<A, B> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).map(f, F);
      }

      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> flatMap(F1<A, _1<Kleisli<F, X, ?>, B>> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).flatMap(f.andThen(k -> (Kleisli<F, X, B>)k), F);
      }
    };
  }

  public static <F, X> Bind<Kleisli<F, X, ?>> monad(final Monad<F> F){
    return new Monad<Kleisli<F, X, ?>>() {
      @Override
      public <A> _1<Kleisli<F, X, ?>, A> point(F0<A> a) {
        return Kleisli.point(a, F);
      }

      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> map(F1<A, B> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).map(f, F);
      }

      @Override
      public <A, B> _1<Kleisli<F, X, ?>, B> flatMap(F1<A, _1<Kleisli<F, X, ?>, B>> f, _1<Kleisli<F, X, ?>, A> fa) {
        return ((Kleisli<F, X, A>)fa).flatMap(f.andThen(k -> (Kleisli<F, X, B>)k), F);
      }
    };
  }

}