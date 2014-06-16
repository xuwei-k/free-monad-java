package free;

public interface Monad<F> extends Applicative<F>, Bind<F> {

  public static interface WithDefault<F> extends Monad<F>, Bind.WithDefault<F> {
    @Override
    public default <A, B> _1<F, B> map(F1<A, B> f, _1<F, A> fa){
      return flatMap(a -> point(() -> f.apply(a)), fa);
    }
  }
}
