package free;

public final class DList<A> implements _1<DList.z, A>{

  DList(F1<List<A>, Free<F0.z, List<A>>> run) {
    this.run = run;
  }

  public static final class z {}

  final F1<List<A>, Free<F0.z, List<A>>> run;

  @SafeVarargs
  public static <X> DList<X> of(final X... as){
    return fromList(List.of(as));
  }

  public static <X> DList<X> single(final X x){
    return fromList(List.single(x));
  }

  public static <X> DList<X> fromList(final List<X> as){
    return new DList<>(bs -> Trampoline.delay(() -> as.append(bs)));
  }

  public static <X> DList<X> empty(){
    return new DList<>(Free::done);
  }

  public DList<A> prepend(final A a){
    return new DList<>(as ->
      Trampoline.suspend(() ->
        run.apply(as).map(list -> list.cons(a))
      )
    );
  }

  public DList<A> append(final F0<DList<A>> as){
    return new DList<>(xs ->
      as.apply().run.apply(xs).flatMap(run::apply)
    );
  }

  public DList<A> append1(final A a){
    return new DList<>(as ->
      Trampoline.suspend(() -> run.apply(as.cons(a)))
    );
  }

  public <B> DList<B> map(final F1<A, B> f){
    return foldr(() -> empty(), (x, y) -> y.apply().prepend(f.apply(x)));
  }

  public <B> DList<B> flatMap(final F1<A, DList<B>> f){
    return this.foldr(() -> empty(), (x, y) -> f.apply(x).append(y));
  }

  public List<A> toList(){
    return Trampoline.run(run.apply(List.nil()));
  }

  public <B> B uncons(final F0<B> z, final F2<A, DList<A>, B> f){
    return Trampoline.run(
      run.apply(List.nil()).flatMap(list ->
        list.cata(
          () -> Trampoline.delay(z),
          (x, xs) ->
            Trampoline.delay(() -> f.apply(x, fromList(xs)))
        )
      )
    );
  }

  private static <X, Y> Free<F0.z, Y> foldr0(final DList<X> xs, final F0<Y> z, final F2<X, F0<Y>, Y> f){
    return Trampoline.suspend(() ->
      xs.uncons(
        () -> Trampoline.delay(z),
        (h, t) -> foldr0(t, z, f).map(x -> f.apply(h, () -> x))
      )
    );
  }

  public <B> B foldr(final F0<B> z, final F2<A, F0<B>, B> f){
    return Trampoline.run(foldr0(this, z, f));
  }

  private static <X> DList<X> narrow(final _1<z, X> a){
    return (DList<X>)a;
  }

  private static Instance instance = new Instance();

  public static MonadPlus<z> monadPlus = instance;

  public static Traverse<z> traverse = instance;

  private static final class Instance implements MonadPlus.WithDefault<z>, Traverse<z> {
    @Override
    public <A> _1<z, A> point(F0<A> a) {
      return new DList<>(bs -> Trampoline.delay(() -> bs.cons(a.apply())));
    }

    @Override
    public <A, B> _1<z, B> flatMap(F1<A, _1<z, B>> f, _1<z, A> fa) {
      return narrow(fa).flatMap(f.andThen(DList::narrow));
    }

    @Override
    public <A> _1<z, A> empty() {
      return DList.empty();
    }

    @Override
    public <A> _1<z, A> plus(_1<z, A> a1, _1<z, A> a2) {
      return narrow(a1).append(() -> narrow(a2));
    }

    @Override
    public <F, A, B> _1<F, _1<z, B>> traverse(_1<z, A> fa, F1<A, _1<F, B>> f, Applicative<F> F) {
      return narrow(fa).<_1<F, _1<z, B>>>foldr(
        () -> F.point(() -> DList.empty()),
        (a, fbs) ->
          F.apply2(
            () -> f.apply(a),
            fbs,
            (x, y) -> narrow(y).prepend(x)
          )
      );
    }

    @Override
    public <A, B> B foldMap(F1<A, B> f, _1<z, A> fa, Monoid<B> B) {
      return narrow(fa).toList().foldMap(f, B); // TODO optimize?
    }

    @Override
    public <A, B> B foldLeft(_1<z, A> fa, B z, F2<B, A, B> f) {
      return narrow(fa).toList().foldLeft(z, f); // TODO optimize?
    }
  }

}
