package free;

public interface Bind<F> extends Apply<F> {
  public <A, B> _1<F, B> flatMap(F1<A, _1<F, B>> f, _1<F, A> fa);

  public static interface WithDefault<F> extends Bind<F> {
    @Override
    public default <A, B> _1<F, B> ap(F0<_1<F, F1<A, B>>> f, F0<_1<F, A>> fa){
      return flatMap(x -> map(x, fa.apply()),f.apply());
    }
  }
}
