package free;

@FunctionalInterface
public interface F0<A> extends _1<F0.z, A> {

  public abstract A apply();

  public default <B> F0<B> map(final F1<A, B> f){
    return () -> f.apply(this.apply());
  }

  public static final class z{}

  public static final Functor<F0.z> functor =
    new Functor<F0.z>() {
      @Override
      public <A, B> F0<B> map(F1<A, B> f, _1<z, A> fa) {
        return ((F0<A>)fa).map(f);
      }
    };

}
