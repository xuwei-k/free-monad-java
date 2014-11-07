package free;

public final class Cofree<F, A> implements _1<Cofree<F, ?>, A> {
  private final A head;
  private final Free<F0.z, _1<F, Cofree<F, A>>> t;

  public Cofree(final A head, final Free<F0.z, _1<F, Cofree<F, A>>> t) {
    this.head = head;
    this.t = t;
  }

  public A head(){
    return head;
  }

  public <B> Cofree<F, B> applyCofree(final F1<A, B> f, final F1<Cofree<F, A>, Cofree<F, B>> g, final Functor<F> F){
    return new Cofree<>(f.apply(head), Free.narrow(Trampoline.monad.map(a -> F.map(g, a), t)));
  }

  public _1<F, Cofree<F, A>> tail(){
    return Trampoline.run(t);
  }

  public <B> Cofree<F, B> map(final F1<A, B> f, final Functor<F> F){
    return applyCofree(f, s -> s.map(f, F), F);
  }

  public <B> Cofree<F, B> extend(final F1<Cofree<F, A>, B> f, final Functor<F> F){
    return applyTail(f.apply(this), s -> s.extend(f, F), F);
  }

  public Cofree<F, Cofree<F, A>> duplicate(final Functor<F> F){
    return applyTail(this, s -> s.duplicate(F), F);
  }

  public <B> Cofree<F, B> inject(final B b, final Functor<F> F){
    return applyTail(b, s -> s.inject(b, F), F);
  }

  public <B> Cofree<F, B> applyTail(final B b, final F1<Cofree<F, A>, Cofree<F, B>> g, final Functor<F> F){
    return applyCofree(x -> b, g, F);
  }

  public static <G, X, Y> Cofree<G, X> unfold(final Y y, final F1<Y, T2<X, _1<G, Y>>> f, final Functor<G> G){
    final T2<X, _1<G, Y>> t = f.apply(y);
    final Free<F0.z, _1<G, Y>> gy = Free.done(t._2);
    return new Cofree<>(t._1, Free.narrow(Trampoline.monad.map(fbb -> G.map(z -> unfold(z, f, G), fbb), gy)));
  }

  public static <G, X> Cofree<G, X> unfoldC(final X x, final F1<X, _1<G, X>> f, final Functor<G> G){
    return delay(x, () -> G.map(z -> unfoldC(z, f, G), f.apply(x)));
  }

  public static <G, X> Cofree<G, X> delay(final X h, final F0<_1<G, Cofree<G, X>>> t){
    return new Cofree<>(h, Trampoline.delay(t));
  }
}
