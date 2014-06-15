package free;

public interface Foldable<F>{
  public <A, B> B foldMap(F1<A, B> f, _1<F, A> fa, Monoid<F> F);
  public <A, B> B foldLeft(_1<F, A> fa, B z, F2<B, A, B> f);
}
