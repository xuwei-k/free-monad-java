package free;

public interface Apply<F> extends Functor<F> {
  public <A, B> _1<F, B> ap(F0<_1<F, F1<A, B>>> f, F0<_1<F, A>> fa);

  public default <A1, A2, Z> _1<F, Z> apply2(final F0<_1<F, A1>> fa1, final F0<_1<F, A2>> fa2, final F2<A1, A2, Z> f){
    return ap(() -> map(a1 -> a2 -> f.apply(a1, a2), fa1.apply()), fa2);
  }

  public default <A1, A2, A3, Z> _1<F, Z> apply3(final F0<_1<F, A1>> fa1, final F0<_1<F, A2>> fa2, final F0<_1<F, A3>> fa3, final F3<A1, A2, A3, Z> f){
    return apply2(() -> apply2(fa1, fa2, T2::new), fa3, (a1a2, a3) -> f.apply(a1a2._1, a1a2._2, a3));
  }

  public default <A1, A2, A3, A4, Z> _1<F, Z> apply4(final F0<_1<F, A1>> fa1, final F0<_1<F, A2>> fa2, final F0<_1<F, A3>> fa3, final F0<_1<F, A4>> fa4, final F4<A1, A2, A3, A4, Z> f){
    return apply2(() -> apply2(fa1, fa2, T2::new), () -> apply2(fa3, fa4, T2::new), (a1a2, a3a4) -> f.apply(a1a2._1, a1a2._2, a3a4._1, a3a4._2));
  }
}
