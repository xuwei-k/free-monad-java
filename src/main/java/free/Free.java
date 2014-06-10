package free;

public abstract class Free<F, A>{
  private Free(){}

  public static <G, B> Free<G, B> done(final B b){
    return new Done<>(b);
  }

  public static <G, B> Free<G, B> liftF(final _1<G, B> value, final Functor<G> G){
    return new Suspend<>(G.map(Done::new, value));
  }

  public abstract <B> Free<F, B> flatMap(final F1<A, Free<F, B>> f);

  // TODO use heap instead of stack
  public final Either<_1<F, Free<F, A>>, A> resume(final Functor<F> F) {
    if(this instanceof Done){
      return Either.<_1<F, Free<F, A>>, A>right(((Done<F, A>)this).a);
    }else if(this instanceof Suspend){
      return Either.<_1<F, Free<F, A>>, A>left(((Suspend<F, A>)this).a);
    }else {
      final Gosub<F, Object, A> gosub1 = (Gosub<F, Object, A>)this;
      if(gosub1.a instanceof Done){
        return
        gosub1.f.apply(((Done<F, A>)gosub1.a).a).resume(F);
      }else if(gosub1.a instanceof Suspend){
        return
        Either.left(F.map(o -> o.flatMap(gosub1.f),((Suspend<F, Object>) gosub1.a).a));
      }else {
        final Gosub<F, Free<F, A>, A> gosub2 = (Gosub<F, Free<F, A>, A>)gosub1.a;
        return
        gosub2.a.flatMap(o ->
          gosub2.f.apply(o).flatMap((F1<A, Free<F, A>>)gosub1.f)
        ).resume(F);
      }
    }
  }

  public final <B> Free<F, B> map(final F1<A, B> f) {
    return flatMap(a -> new Done<>(f.apply(a)));
  }

  private static final class Done<F, A> extends Free<F, A>{
    private final A a;

    private Done(final A a) {
      this.a = a;
    }

    @Override
    public <B> Free<F, B> flatMap(F1<A, Free<F, B>> f) {
      return new Gosub<>(this, f);
    }
  }

  private static final class Suspend<F, A> extends Free<F, A>{
    private final _1<F, Free<F, A>> a;

    private Suspend(final _1<F, Free<F, A>> a) {
      this.a = a;
    }

    @Override
    public <B> Free<F, B> flatMap(F1<A, Free<F, B>> f) {
      return new Gosub<>(this, f);
    }
  }

  private static final class Gosub<F, A, B> extends Free<F, B>{
    private final Free<F, A> a;
    private final F1<A, Free<F, B>> f;

    private Gosub(final Free<F, A> a, final F1<A, Free<F, B>> f){
      this.a = a;
      this.f = f;
    }

    @Override
    public <C> Free<F, C> flatMap(final F1<B, Free<F, C>> g) {
      return new Gosub<>(a, aa -> new Gosub<>(f.apply(aa), g));
    }
  }

}

