package free;

public final class Id<A> implements _1<Id.z, A> {
  static final class z{}
  public final A value;
  public Id(A value) {
    this.value = value;
  }
  public <B> Id<B> map(final F1<A, B> f){
    return new Id<>(f.apply(value));
  }

  public <B> Id<B> flatMap(final F1<A, Id<B>> f){
    return f.apply(value);
  }

  public static Monad<z> monad =
    new Monad<z>() {
      @Override
      public <A, B> _1<z, B> map(F1<A, B> f, _1<z, A> fa) {
        return ((Id<A>)fa).map(f);
      }

      @Override
      public <A> _1<z, A> point(F0<A> a) {
        return new Id<>(a.apply());
      }

      @Override
      public <A, B> _1<z, B> flatMap(F1<A, _1<z, B>> f, _1<z, A> fa) {
        return ((Id<A>)fa).flatMap(a -> (Id<B>)f.apply(a));
      }
    };
}

