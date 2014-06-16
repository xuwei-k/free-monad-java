package free;

public final class WriterT<F, W, A> implements _1<WriterT<F, W, ?>, A>{
  public final _1<F, T2<W, A>> run;

  public WriterT(_1<F, T2<W, A>> run) {
    this.run = run;
  }

  public <B> WriterT<F, W, B> map(final F1<A, B> f, final Functor<F> F){
    return new WriterT<>(F.map(t -> new T2<>(t._1, f.apply(t._2)), run));
  }

  public <B> WriterT<F, W, B> flatMap(final F1<A, WriterT<F, W, B>> f, final Bind<F> F, final Semigroup<W> W){
    return
    new WriterT<>(F.flatMap(wa ->
      F.map(wb ->
        new T2<>(W.append(wa._1, wb._1), wb._2)
        ,f.apply(wa._2).run
      )
    , run));
  }

  public <B> WriterT<F, W, B> ap(final F0<WriterT<F, W, F1<A, B>>> f, final Apply<F> F, final Semigroup<W> W){
    return new WriterT<>(
      F.apply2(() -> f.apply().run, () -> run, (x, y) ->
        new T2<>(W.append(x._1, y._1), x._2.apply(y._2))
      )
    );
  }

  public _1<F, W> written(final Functor<F> F){
    return F.map(T2.__1(), run);
  }

  public _1<F, A> value(final Functor<F> F){
    return F.map(T2.__2(), run);
  }

  public <X, B> WriterT<F, X, B> mapValue(final F1<T2<W, A>, T2<X, B>> f, final Functor<F> F){
    return new WriterT<>(F.map(f, run));
  }

  public <X> WriterT<F, X, A> mapWritten(final F1<W, X> f, final Functor<F> F){
    return mapValue(wa -> new T2<>(f.apply(wa._1), wa._2), F);
  }

  public WriterT<F, W, A> add(final W w1, final Functor<F> F, final Semigroup<W> W){
    return mapWritten(w2 -> W.append(w2, w1), F);
  }

  public static <F, W> Functor<WriterT<F, W, ?>> functor(final Functor<F> F){
    return new WriterTFunctor<F, W>(){
      @Override
      public Functor<F> F(){
        return F;
      }
    };
  }

  public static <F, W> Apply<WriterT<F, W, ?>> apply(final Semigroup<W> W, final Apply<F> F) {
    return new WriterTApply<F, W>(){
      @Override
      public Apply<F> F(){
        return F;
      }
      @Override
      public Semigroup<W> W(){
        return W;
      }
    };
  }

  public static <F, W> Applicative<WriterT<F, W, ?>> apply(final Monoid<W> W, final Applicative<F> F) {
    return new WriterTApplicative<F, W>(){
      @Override
      public Applicative<F> F(){
        return F;
      }
      @Override
      public Monoid<W> W(){
        return W;
      }
    };
  }

  public static <F, W> Bind<WriterT<F, W, ?>> bind(final Semigroup<W> W, final Bind<F> F) {
    return new WriterTBind<F, W>(){
      @Override
      public Bind<F> F(){
        return F;
      }
      @Override
      public Semigroup<W> W(){
        return W;
      }
    };
  }

  public static <F, W> Monad<WriterT<F, W, ?>> monad(final Monoid<W> W, final Monad<F> F) {
    return new WriterTMonad<>(F, W);
  }

  private static interface WriterTFunctor<F, W> extends Functor<WriterT<F, W, ?>>{
    public Functor<F> F();
    @Override
    public default <A, B> _1<WriterT<F, W, ?>, B> map(F1<A, B> f, _1<WriterT<F, W, ?>, A> fa) {
      return ((WriterT<F, W, A>)fa).map(f, F());
    }
  }

  private static interface WriterTApply<F, W> extends WriterTFunctor<F, W>, Apply<WriterT<F, W, ?>> {
    @Override
    public Apply<F> F();
    public Semigroup<W> W();

    @Override
    @SuppressWarnings("unchecked")
    public default <A, B> _1<WriterT<F, W, ?>, B> ap(F0<_1<WriterT<F, W, ?>, F1<A, B>>> f, F0<_1<WriterT<F, W, ?>, A>> fa) {
      return ((WriterT<F, W, A>)fa.apply()).ap((F0<WriterT<F, W, F1<A, B>>>)(Object)f, F(), W());
    }
  }

  private static interface WriterTApplicative<F, W> extends WriterTApply<F, W>, Applicative<WriterT<F, W, ?>> {
    @Override
    public Applicative<F> F();
    @Override
    public Monoid<W> W();

    @Override
    public default <A> _1<WriterT<F, W, ?>, A> point(F0<A> a){
      return new WriterT<>(F().point(() -> new T2<>(W().zero(), a.apply())));
    }
  }

  private static interface WriterTBind<F, W> extends WriterTApply<F, W>, Bind<WriterT<F, W, ?>> {
    @Override
    public Bind<F> F();
    @Override
    public Semigroup<W> W();

    @Override
    public default <A, B> _1<WriterT<F, W, ?>, B> flatMap(F1<A, _1<WriterT<F, W, ?>, B>> f, _1<WriterT<F, W, ?>, A> fa) {
      return ((WriterT<F, W, A>)fa).flatMap(a -> (WriterT<F, W, B>)f.apply(a), F(), W());
    }
  }

  private static final class WriterTMonad<F, W> implements WriterTApplicative<F, W>, WriterTBind<F, W>, Monad<WriterT<F, W, ?>> {
    private final Monad<F> F;
    private final Monoid<W> W;
    private WriterTMonad(Monad<F> f, Monoid<W> w) {
      F = f;
      W = w;
    }
    @Override
    public Monad<F> F() {
      return F;
    }
    @Override
    public Monoid<W> W() {
      return W;
    }
  }


}
