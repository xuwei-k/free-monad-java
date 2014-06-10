package free;

public abstract class Free<F, A>{
  private Free(){}

  private static <X, Y> Y let(final X x, final F1<X, Y> f){
    return f.apply(x);
  }

  public static <G, B> Free<G, B> done(final B b){
    return new Done<>(b);
  }

  public static <G, B> Free<G, B> liftF(final _1<G, B> value, final Functor<G> G){
    return new Suspend<>(G.map(Done::new, value));
  }

  @SuppressWarnings("unchecked")
  private <X> Gosub<F, X, A> asGosub(){
    return (Gosub<F, X, A>)this;
  }

  private Suspend<F, A> asSuspend(){
    return (Suspend<F, A>)this;
  }

  private Done<F, A> asDone(){
    return (Done<F, A>)this;
  }

  public abstract <B> Free<F, B> flatMap(final F1<A, Free<F, B>> f);

  // TODO use heap instead of stack
  public final Either<_1<F, Free<F, A>>, A> resume(final Functor<F> F) {
    if(this instanceof Done){
      return Either.right(this.asDone().a);
    }else if(this instanceof Suspend){
      return Either.left(this.asSuspend().a);
    }else {
      return
      let(this.asGosub(), gosub1 -> {
        if(gosub1.a instanceof Done){
          return
          gosub1.f.apply(gosub1.a.asDone().a).resume(F);
        }else if(gosub1.a instanceof Suspend){
          return
          Either.left(F.map(o -> o.flatMap(gosub1.f), gosub1.a.asSuspend().a));
        }else {
          return
          let(gosub1.a.asGosub(), gosub2 ->
            gosub2.a.flatMap(o ->
              gosub2.f.apply(o).flatMap(gosub1.f)
            ).resume(F)
          );
        }
      });
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

