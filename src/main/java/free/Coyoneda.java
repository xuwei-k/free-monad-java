package free;

final public class Coyoneda<F, A> implements _1<Coyoneda<F, ?>, A>{
  final private _1<F, Object> fi;
  final private F1<Object, A> k;

  @SuppressWarnings("unchecked")
  public <X, Y> Y with(final F2<_1<F, X>, F1<X, A>, Y> f){
    return f.apply((_1<F, X>)fi, (F1<X, A>)k);
  }

  public static <S, B> Coyoneda<S, B> lift(final _1<S, B> s) {
    return new Coyoneda<>(s, F1.id());
  }

  public static final class z{}

  public <B> Coyoneda<F, B> map(final F1<A, B> f){
    return new Coyoneda<F, B>(fi, k.andThen(f));
  }

  @SuppressWarnings("unchecked")
  public <X> Coyoneda(_1<F, X> fi, F1<X, A> k) {
    this.fi = (_1<F, Object>) fi;
    this.k = (F1<Object, A>) k;
  }

  public _1<F, A> run(final Functor<F> F){
    return F.map(k, fi);
  }

  public static <G> Functor<Coyoneda<G, ?>> functor(){
    return
    new Functor<Coyoneda<G, ?>>() {
      @Override
      public <A, B> _1<Coyoneda<G, ?>, B> map(F1<A, B> f, _1<Coyoneda<G, ?>, A> fa) {
        return ((Coyoneda<G, A>)fa).map(f);
      }
    };
  }

}


