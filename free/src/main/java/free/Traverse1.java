package free;

public interface Traverse1<F> extends Traverse<F>, Foldable1<F> {

  public <G, A, B> _1<G, _1<F, B>> traverse1(_1<F, A> fa, F1<A, _1<G, B>> f, Apply<G> G);

  @Override
  public default <G, A, B> _1<G, _1<F, B>> traverse(_1<F, A> fa, F1<A, _1<G, B>> f, Applicative<G> G){
    return traverse1(fa, f, G);
  }

  public default <G, A> _1<G, _1<F, A>> sequence1(_1<F, _1<G, A>> fga, Apply<G> G){
    return traverse1(fga, F1.id(), G);
  }

}
