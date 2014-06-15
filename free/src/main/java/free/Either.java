package free;

public abstract class Either<A, B> implements _1<Either<A, ?>, B> {
  private Either(){}

  public <C, D> Either<C, D> bimap(final F1<A, C> f, final F1<B, D> g) {
    return fold(l -> left(f.apply(l)), r -> right(g.apply(r)));
  }

  public Either<B, A> swap(){
    return fold(Either::right, Either::left);
  }

  public abstract <X> X fold(F1<A, X> left, F1<B, X> right);

  public boolean isRight(){
    return this instanceof Right;
  }

  public boolean isLeft(){
    return this instanceof Left;
  }

  @SuppressWarnings("unchecked")
  public final <C> Either<A, C> map(final F1<B, C> f){
    if(isLeft()){
      return (Either<A, C>)this;
    }else{
      return right(f.apply(rightOrNull()));
    }
  }

  @SuppressWarnings("unchecked")
  public final <C> Either<A, C> flatMap(final F1<B, Either<A, C>> f){
    if(isLeft()){
      return (Either<A, C>)this;
    }else{
      return f.apply(rightOrNull());
    }
  }

  final B rightOrNull(){
    if(isRight()){
      return ((Right<A, B>) this).value;
    }else {
      return null;
    }
  }

  final A leftOrNull(){
    if(isLeft()){
      return ((Left<A, B>) this).value;
    }else {
      return null;
    }
  }

  public static <X, Y> Either<X, Y> left(final X x){
    return new Left<>(x);
  }

  public static <X, Y> Either<X, Y> right(final Y y){
    return new Right<>(y);
  }

  public static <L> Monad<Either<L, ?>> monad(){
    return new Monad<Either<L, ?>>() {
      @Override
      public <A> _1<Either<L, ?>, A> point(F0<A> a) {
        return right(a.apply());
      }

      @Override
      public <A, B> _1<Either<L, ?>, B> flatMap(F1<A, _1<Either<L, ?>, B>> f, _1<Either<L, ?>, A> fa) {
        return ((Either<L, A>)fa).flatMap(x -> (Either<L, B>)f.apply(x));
      }
    };
  }

  private final static class Left<X, Y> extends Either<X, Y>{
    private final X value;
    private Left(final X value) {
      this.value = value;
    }

    @Override
    public <X1> X1 fold(F1<X, X1> left, F1<Y, X1> right) {
      return left.apply(value);
    }
  }

  private final static class Right<X, Y> extends Either<X, Y>{
    private final Y value;
    private Right(final Y value) {
      this.value = value;
    }

    @Override
    public <X1> X1 fold(F1<X, X1> left, F1<Y, X1> right) {
      return right.apply(value);
    }
  }
}
