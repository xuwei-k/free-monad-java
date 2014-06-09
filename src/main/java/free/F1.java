package free;

public abstract class F1<A, B> {

  public static <X> F1<X, X> id() {
    return new F1<X, X>() {
      @Override
      public X apply(final X x) {
        return x;
      }
    };
  }

  public abstract B apply(A a);
}
