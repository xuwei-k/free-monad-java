package free;

@FunctionalInterface
public interface F2<A, B, C> {
  public abstract C apply(A a, B b);
}
