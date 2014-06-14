package free;

@FunctionalInterface
public interface F1<A, B> extends _1<F1<A, ?>, B>{

  public static <X> F1<X, X> id() {
    return x -> x;
  }

  public abstract B apply(A a);

  public default <C> F1<A, C> andThen(final F1<B, C> f){
    return a -> f.apply(apply(a));
  }
}
