package free;

import lombok.AllArgsConstructor;

public abstract class Either<A, B>{
  private Either(){}

  public abstract <X> X fold(F1<A, X> left, F1<B, X> right);

  public static <X, Y> Either<X, Y> left(final X x){
    return new Left<>(x);
  }

  public static <X, Y> Either<X, Y> right(final Y y){
    return new Right<>(y);
  }

  @AllArgsConstructor
  private final static class Left<X, Y> extends Either<X, Y>{
    private final X value;

    @Override
    public <X1> X1 fold(F1<X, X1> left, F1<Y, X1> right) {
      return left.apply(value);
    }
  }

  @AllArgsConstructor
  private final static class Right<X, Y> extends Either<X, Y>{
    private final Y value;

    @Override
    public <X1> X1 fold(F1<X, X1> left, F1<Y, X1> right) {
      return right.apply(value);
    }
  }
}
