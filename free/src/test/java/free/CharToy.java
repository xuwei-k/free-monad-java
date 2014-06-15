package free;

/**
 * http://eed3si9n.com/learning-scalaz/ja/Free+Monad.html
 */
abstract class CharToy<A> implements _1<CharToy.z, A>{
  public abstract <Z> Z fold(F2<Character, A, Z> output, F1<A, Z> bell, Z done);

  public static Free<CharToy.z, Unit> output(final char a){
    return Free.liftF(new CharOutput<>(a, Unit.unit), functor);
  }
  public static Free<CharToy.z, Unit> bell(){
    return Free.liftF(new CharBell<Unit>(Unit.unit), functor);
  }
  public static Free<CharToy.z, Unit> done(){
    return Free.liftF(new CharDone<Unit>(), functor);
  }
  public static <A> Free<CharToy.z, A> pointed(final A a){
    return Free.done(a);
  }
  public abstract <B> CharToy<B> map(F1<A, B> f);
  private CharToy(){}
  /**
   * https://github.com/svn2github/highj/blob/108f9f936/trunk/src/main/java/org/highj/data/collection/List.java#L27
   */
  public static final class z{}

  public static final Functor<CharToy.z> functor =
    new Functor<CharToy.z>() {
      @Override
      public <X, Y> _1<CharToy.z, Y> map(F1<X, Y> f, _1<CharToy.z, X> fa) {
        return ((CharToy<X>)fa).map(f);
      }
    };

  private static final class CharOutput<A> extends CharToy<A>{
    private final char a;
    private final A next;
    private CharOutput(final char a, final A next) {
      this.a = a;
      this.next = next;
    }

    @Override
    public <Z> Z fold(final F2<Character, A, Z> output, final F1<A, Z> bell, final Z done) {
      return output.apply(a, next);
    }

    @Override
    public <B> CharToy<B> map(final F1<A, B> f) {
      return new CharOutput<>(a, f.apply(next));
    }
  }

  private static final class CharBell<A> extends CharToy<A> {
    private final A next;
    private CharBell(final A next) {
      this.next = next;
    }

    @Override
    public <Z> Z fold(final F2<Character, A, Z> output, final F1<A, Z> bell, Z done) {
      return bell.apply(next);
    }

    @Override
    public <B> CharToy<B> map(final F1<A, B> f) {
      return new CharBell<>(f.apply(next));
    }
  }

  private static final class CharDone<A> extends CharToy<A> {
    @Override
    public <Z> Z fold(final F2<Character, A, Z> output, final F1<A, Z> bell, final Z done) {
      return done;
    }

    @Override
    public <B> CharToy<B> map(final F1<A, B> f) {
      return new CharDone<>();
    }
  }
}

