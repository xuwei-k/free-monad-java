package free;

public final class ListT<F, A> implements _1<ListT<F, ?>, A> {
  public final _1<F, List<A>> run;

  public ListT(_1<F, List<A>> run) {
    this.run = run;
  }

  public <B> ListT<F, B> map(final F1<A, B> f, final Functor<F> F){
    return new ListT<>(F.map(a -> a.map(f), run));
  }

  public <B> ListT<F, B> flatMap(final F1<A, ListT<F, B>> f, final Monad<F> F){
    return new ListT<>(F.flatMap(list ->
      list.<_1<F, List<B>>>cata(
        () -> F.point(() -> List.nil()),
        (h, t) -> t.foldLeft(f.apply(h), (fb, a) -> fb.append(f.apply(a), F)).run
      )
    , run));
  }

  public ListT<F, A> cons(final A head, final Functor<F> F){
    return new ListT<>(F.map(list -> list.cons(head), run));
  }

  public ListT<F, A> append(final ListT<F, A> tail, final Bind<F> F){
    return new ListT<>(F.flatMap(list1 ->
      F.map(list1::append, tail.run)
    , run));
  }

  public static <F, A> ListT<F, A> empty(final Applicative<F> F){
    return new ListT<>(F.point(() -> List.nil()));
  }

  public static <F> Functor<ListT<F, ?>> functor(final Functor<F> F){
    return new Functor<ListT<F, ?>>() {
      @Override
      public <A, B> _1<ListT<F, ?>, B> map(F1<A, B> f, _1<ListT<F, ?>, A> fa) {
        return ((ListT<F, A>)fa).map(f, F);
      }
    };
  }

  public static <F> MonadPlus<ListT<F, ?>> functor(final Monad<F> F){
    return new MonadPlus.WithDefault<ListT<F, ?>>() {
      @Override
      public <A, B> _1<ListT<F, ?>, B> map(F1<A, B> f, _1<ListT<F, ?>, A> fa) {
        return ((ListT<F, A>)fa).map(f, F);
      }

      @Override
      public <A> _1<ListT<F, ?>, A> point(F0<A> a) {
        return ListT.<F, A>empty(F).cons(a.apply(), F);
      }

      @Override
      public <A, B> _1<ListT<F, ?>, B> flatMap(F1<A, _1<ListT<F, ?>, B>> f, _1<ListT<F, ?>, A> fa) {
        return ((ListT<F, A>)fa).flatMap(a -> (ListT<F, B>)f.apply(a), F);
      }

      @Override
      public <A> _1<ListT<F, ?>, A> empty() {
        return ListT.empty(F);
      }

      @Override
      public <A> _1<ListT<F, ?>, A> plus(_1<ListT<F, ?>, A> a1, _1<ListT<F, ?>, A> a2) {
        return ((ListT<F, A>)a1).append((ListT<F, A>)a2, F);
      }
    };
  }

}
