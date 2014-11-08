package free;

public interface Comonad<F> extends Cobind<F> {
  public <A> A copoint(_1<F, A> fa);
}
