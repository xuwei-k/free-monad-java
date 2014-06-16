package free;

@FunctionalInterface
public interface F0<A> extends _1<F0.z, A> {

  public abstract A apply();

  public default <B> F0<B> map(final F1<A, B> f) {
    return () -> f.apply(this.apply());
  }

  public default <B> F0<B> flatMap(final F1<A, F0<B>> f) {
    return () -> f.apply(this.apply()).apply();
  }

  public default <G, B> _1<G, F0<B>> traverse1(final F1<A, _1<G, B>> f, Apply<G> G) {
    return G.map(b -> () -> b, f.apply(this.apply()));
  }

  public static final class z {}

  public static final Monad<z> monad = Instance.value;
  public static final Traverse1<z> traverse1 = Instance.value;

  static final class Instance implements Monad.WithDefault<z>, Traverse1<F0.z> {
    private Instance(){}
    private static final Instance value = new Instance();

    @Override
    public <A> _1<z, A> point(F0<A> a) {
      return a;
    }

    @Override
    public <A, B> F0<B> map(F1<A, B> f, _1<z, A> fa) {
      return ((F0<A>) fa).map(f);
    }

    @Override
    public <A, B> _1<z, B> flatMap(F1<A, _1<z, B>> f, _1<z, A> fa) {
      return ((F0<A>)fa).flatMap(a -> (F0<B>)f.apply(a));
    }

    @Override
    public <G, A, B> _1<G, _1<z, B>> traverse1(_1<z, A> fa, F1<A, _1<G, B>> f, Apply<G> G) {
      return G.map((F0<B> b) -> b, ((F0<A>) fa).traverse1(f, G));
    }

    @Override
    public <A, B> B foldMap1(F1<A, B> f, _1<z, A> fa, Semigroup<B> B) {
      return f.apply(((F0<A>)fa).apply());
    }

    @Override
    public <A, B> B foldMapLeft1(_1<z, A> fa, F1<A, B> z, F2<B, A, B> f) {
      return z.apply(((F0<A>) fa).apply());
    }

    @Override
    public <A, B> B foldLeft(_1<z, A> fa, B z, F2<B, A, B> f) {
      return f.apply(z, ((F0<A>)fa).apply());
    }
  }
}
