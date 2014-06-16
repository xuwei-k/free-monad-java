package free;

public final class Trampoline {

  private Trampoline(){
    throw new UnsupportedOperationException();
  }

  public static <A> Free<F0.z, A> delay(final F0<A> f){
    return suspend(() -> Free.done(f.apply()));
  }

  public static <A> Free<F0.z, A> suspend(final F0<Free<F0.z, A>> f){
    return Free.suspend(f);
  }

  public static <A> A run(final Free<F0.z, A> f){
    return f.go(a -> ((F0<Free<F0.z, A>>)a).apply(), F0.monad);
  }

  public static final Monad<Free<F0.z, ?>> monad =
    Free.freeMonad(F0.monad);

}
