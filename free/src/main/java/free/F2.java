package free;

@FunctionalInterface
public interface F2<A, B, C> {
  public abstract C apply(A a, B b);

  public default F2<B, A, C> swap(){
    return (b, a) -> apply(a, b);
  }
}
