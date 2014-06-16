package free;

@FunctionalInterface
public interface F1<A, B> extends _1<F1<A, ?>, B>{

  public static <X> F1<X, X> id() {
    return x -> x;
  }

  public abstract B apply(A a);

  public default <C> F1<A, C> map(final F1<B, C> f){
    return a -> f.apply(apply(a));
  }

  public default <C> F1<A, C> andThen(final F1<B, C> f){
    return map(f);
  }

  public default <C> F1<A, C> flatMap(final F1<B, F1<A, C>> f){
    return (A a) -> f.apply(this.apply(a)).apply(a);
  }

  public static <X, Y> F1<X, Y> constant(final Y y){
    return x -> y;
  }

  public static <X> Monad<F1<X, ?>> monad(){
    return new Instance<>();
  }

  static final class Instance<X> implements Monad<F1<X, ?>> {
    private Instance(){}

    @Override
    public <A> _1<F1<X, ?>, A> point(F0<A> a) {
      final F1<X, A> f = x -> a.apply();
      return f;
    }

    @Override
    public <A, B> _1<F1<X, ?>, B> map(F1<A, B> f, _1<F1<X, ?>, A> fa) {
      return ((F1<X, A>)fa).map(f);
    }

    @Override
    public <A, B> _1<F1<X, ?>, B> flatMap(F1<A, _1<F1<X, ?>, B>> f, _1<F1<X, ?>, A> fa) {
      return ((F1<X, A>)fa).flatMap(a -> (F1<X, B>)f.apply(a));
    }
  }
}
