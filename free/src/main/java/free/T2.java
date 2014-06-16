package free;

final public class T2<A1, A2> {
  public final A1 _1;
  public final A2 _2;

  public T2(A1 a1, A2 a2) {
    _1 = a1;
    _2 = a2;
  }

  public static <X1, X2> F1<T2<X1, X2>, X1> __1(){
    return t -> t._1;
  }

  public static <X1, X2> F1<T2<X1, X2>, X2> __2(){
    return t -> t._2;
  }
}