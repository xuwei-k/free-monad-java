package free;

public interface Traverse<F> extends Foldable<F>, Functor<F> {

  public <G, A, B> _1<G, _1<F, B>> traverse(_1<F, A> fa, F1<A, _1<G, B>> f, Applicative<G> G);

  public default <G, A> _1<G, _1<F, A>> sequence(_1<F, _1<G, A>> fga, Applicative<G> G){
    return traverse(fga, F1.id(), G);
  }

}
