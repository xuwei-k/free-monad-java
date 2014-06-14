package free;

/**
 * Natural Transformation
 */
public interface NT<F, G> {
  public <A> _1<G, A> apply(_1<F, A> fa);

  public static <X> NT<X, X> id(){
    return new NT<X, X>() {
      @Override
      public <A> _1<X, A> apply(_1<X, A> fa) {
        return fa;
      }
    };
  }
}
