package free;

public interface Monad<F> extends Functor<F> {
  @Override
  public default <A, B> _1<F, B> map(F1<A, B> f, _1<F, A> fa){
    return flatMap(a -> point(() -> f.apply(a)), fa);
  }
  public <A> _1<F, A> point(final F0<A> a);
  public <A, B> _1<F, B> flatMap(F1<A, _1<F, B>> f, _1<F, A> fa);
}
