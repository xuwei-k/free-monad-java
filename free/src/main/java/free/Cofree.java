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

  public <B> Cofree<F, B> flatMap(final F1<A, Cofree<F, B>> f, final Functor<F> F, final Plus<F> G){
    final Cofree<F, B> c = narrow(f.apply(head));
    return new Cofree<F, B>(
      c.head,
      c.t.map(ct -> G.plus(c.tail(), F.map(x -> x.flatMap(f, F, G), tail())))
    );
  }

  public <B> B foldMap1(final F1<A, B> f, final Foldable1<F> F, final Semigroup<B> G){
    return G.append(f.apply(head), F.foldMap1(x -> x.foldMap1(f, F, G), tail(), G));
  }

  public <B> B foldLeft(final B z, final F2<B, A, B> f, final Foldable<F> F) {
    return F.foldLeft(tail(), f.apply(z, head), (b, c) -> c.foldLeft(b, f, F));
  }

  public <B> B foldMapLeft1(final F1<A, B> z, final F2<B, A, B> f, final Foldable<F> F) {
    return F.foldLeft(tail(), z.apply(head), (b, c) -> c.foldLeft(b, f, F));
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

  public static <G, X> Cofree<G, X> narrow(final _1<Cofree<G, ?>, X> fa){
    return (Cofree<G, X>)fa;
  }

  public static <G, X> Cofree<G, X> point(final X x, final PlusEmpty<G> G){
    return new Cofree<>(x, Free.done(G.empty()));
  }

  public static <F> Comonad<Cofree<F, ?>> comonad(final Functor<F> F){
    return new Comonad<Cofree<F, ?>>(){

      @Override
      public <A, B> _1<Cofree<F, ?>, B> map(F1<A, B> f, _1<Cofree<F, ?>, A> fa) {
        return narrow(fa).map(f, F);
      }

      @Override
      public <A, B> _1<Cofree<F, ?>, B> cobind(F1<_1<Cofree<F, ?>, A>, B> f, _1<Cofree<F, ?>, A> fa) {
        return narrow(fa).extend(f::apply, F);
      }

      @Override
      public <A> A copoint(_1<Cofree<F, ?>, A> fa) {
        return narrow(fa).head;
      }
    };
  }

  public static <F> Bind<Cofree<F, ?>> bind(final Functor<F> F, final Plus<F> G){
    return new Bind.WithDefault<Cofree<F, ?>>() {
      @Override
      public <A, B> _1<Cofree<F, ?>, B> flatMap(F1<A, _1<Cofree<F, ?>, B>> f, _1<Cofree<F, ?>, A> fa) {
        return narrow(fa).flatMap(f.map(Cofree::narrow), F, G);
      }

      @Override
      public <A, B> _1<Cofree<F, ?>, B> map(F1<A, B> f, _1<Cofree<F, ?>, A> fa) {
        return narrow(fa).map(f, F);
      }
    };
  }

  public static <F> Monad<Cofree<F, ?>> monad(final Functor<F> F, final PlusEmpty<F> G){
    return new Monad.WithDefault<Cofree<F, ?>>() {
      @Override
      public <A> _1<Cofree<F, ?>, A> point(F0<A> a) {
        return Cofree.point(a.apply(), G);
      }

      @Override
      public <A, B> _1<Cofree<F, ?>, B> flatMap(F1<A, _1<Cofree<F, ?>, B>> f, _1<Cofree<F, ?>, A> fa) {
        return narrow(fa).flatMap(f.map(Cofree::narrow), F, G);
      }

      @Override
      public <A, B> _1<Cofree<F, ?>, B> map(F1<A, B> f, _1<Cofree<F, ?>, A> fa) {
        return narrow(fa).map(f, F);
      }
    };
  }

  public static <F> Foldable1<Cofree<F, ?>> foldable1(final Foldable1<F> F){
    return new Foldable1<Cofree<F, ?>>() {
      @Override
      public <A, B> B foldMap1(F1<A, B> f, _1<Cofree<F, ?>, A> fa, Semigroup<B> B) {
        return narrow(fa).foldMap1(f, F, B);
      }

      @Override
      public <A, B> B foldMapLeft1(_1<Cofree<F, ?>, A> fa, F1<A, B> z, F2<B, A, B> f) {
        return narrow(fa).foldMapLeft1(z, f, F);
      }

      @Override
      public <A, B> B foldLeft(_1<Cofree<F, ?>, A> fa, B z, F2<B, A, B> f) {
        return narrow(fa).foldLeft(z, f, F);
      }
    };
  }
}
