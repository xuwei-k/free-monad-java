package free;

public interface Cobind<F> extends Functor<F> {
  public <A, B> _1<F, B> cobind(F1<_1<F, A>, B> f, _1<F, A> fa);

  public default <A> _1<F, _1<F, A>> cojoin(_1<F, A> fa){
    return cobind(F1.id(), fa);
  }
}
