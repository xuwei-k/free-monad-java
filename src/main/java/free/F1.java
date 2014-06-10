package free;

public interface F1<A, B> {

  public static <X> F1<X, X> id() {
    return x -> x;
  }

  public abstract B apply(A a);
}
