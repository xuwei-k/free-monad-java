package free;

public interface Functor<F> {
  public <A, B> _1<F, B> map(F1<A, B> f, _1<F, A> fa);
}
