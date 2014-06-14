package free;

public interface Applicative<F> extends Apply<F> {

  public <A> _1<F, A> point(F0<A> a);

  @Override
  public default <A, B> _1<F, B> map(F1<A, B> f, _1<F, A> fa){
    return ap(() -> point(() -> f), () -> fa);
  }

}
