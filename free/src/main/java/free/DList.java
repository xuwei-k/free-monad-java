package free;

public final class DList<A> implements _1<DList.x, A>{

  DList(F1<List<A>, Free<F0.z, List<A>>> run) {
    this.run = run;
  }

  public static final class x {}

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

}
