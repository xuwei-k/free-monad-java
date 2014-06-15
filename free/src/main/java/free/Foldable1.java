package free;

public interface Foldable1<F> extends Foldable<F>{
  public <A, B> B foldMap1(F1<A, B> f, _1<F, A> fa, Semigroup<B> B);
  @Override
  public default <A, B> B foldMap(F1<A, B> f, _1<F, A> fa, Monoid<B> B){
    return foldMap1(f, fa, B);
  }
  public <A, B> B foldLeft1(_1<F, A> fa, F2<B, A, B> f);
}
