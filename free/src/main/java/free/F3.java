package free;

@FunctionalInterface
public interface F3<A1, A2, A3, Z> {
  public abstract Z apply(A1 a1, A2 a2, A3 a3);
}
