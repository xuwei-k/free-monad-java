package free;

public interface Foldable1<F> extends Foldable<F>{
  public <A, B> B foldMap1(F1<A, B> f, _1<F, A> fa, Semigroup<F> F);
  @Override
  public default <A, B> B foldMap(F1<A, B> f, _1<F, A> fa, Monoid<F> F){
    return foldMap1(f, fa, F);
  }
  public <A, B> B foldLeft1(_1<F, A> fa, F2<B, A, B> f);
}
