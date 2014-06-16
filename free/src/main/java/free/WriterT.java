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
}
