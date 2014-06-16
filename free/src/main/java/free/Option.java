package free;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Option<A> implements Iterable<A>, _1<Option.z, A> {
  public static final class z{}

  private Option(){}

  public abstract <B> Option<B> map(final F1<A, B> f);

  public abstract <B> B fold(final F1<A, B> f, final F0<B> z);

  public abstract <B> Option<B> flatMap(final F1<A, Option<B>> f);

  public abstract Option<A> orElse(final Option<A> a);

  public final boolean isEmpty(){
    return this instanceof None;
  }

  public final boolean isDefined(){
    return this instanceof Some;
  }

  public abstract <E> Either<E, A> toRight(F0<E> left);

  public abstract <E> Either<A, E> toLeft(F0<E> right);

  public abstract A getOrElse(final F0<A> a);

  public abstract A getOrThrow(final RuntimeException e);

  public abstract A getOrThrow(final String errorMessage);

  private static Instance instance = new Instance();

  public static MonadPlus<Option.z> monadPlus = instance;

  public static Traverse<Option.z> traverse = instance;

  private static final class Instance implements MonadPlus.WithDefault<z>, Traverse<z> {

    @Override
    public <A> _1<z, A> point(F0<A> a) {
      return new Some<>(a.apply());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A, B> _1<z, B> flatMap(F1<A, _1<z, B>> f, _1<z, A> fa) {
      return ((Option<A>)fa).flatMap((F1<A, Option<B>>)((Object)f));
    }

    @Override
    public <A, B> _1<z, B> map(F1<A, B> f, _1<z, A> fa) {
      return ((Option<A>)fa).map(f);
    }

    @Override
    public <A> _1<z, A> empty() {
      return none();
    }

    @Override
    public <A> _1<z, A> plus(_1<z, A> a1, _1<z, A> a2) {
      return ((Option<A>)a1).orElse((Option<A>)a2);
    }

    @Override
    public <A, B> B foldMap(F1<A, B> f, _1<z, A> fa, Monoid<B> B) {
      return ((Option<A>)fa).fold(f, () -> B.zero());
    }

    @Override
    public <A, B> B foldLeft(_1<z, A> fa, B z, F2<B, A, B> f) {
      return ((Option<A>)fa).fold(a -> f.apply(z, a), () -> z);
    }

    @Override
    public <G, A, B> _1<G, _1<z, B>> traverse(_1<z, A> fa, F1<A, _1<G, B>> f, Applicative<G> G) {
      return ((Option<A>)fa).map(a ->
        G.map(b -> (_1<z, B>)Option.some(b), f.apply(a))
      ).getOrElse(() -> G.point(() -> Option.none()));
    }
  }

  private static final class Some<A> extends Option<A>{
    private final A value;

    private Some(A value) {
      this.value = value;
    }

    @Override
    public <B> Option<B> map(F1<A, B> f) {
      return new Some<>(f.apply(value));
    }

    @Override
    public <B> B fold(F1<A, B> f, F0<B> z) {
      return f.apply(value);
    }

    @Override
    public <B> Option<B> flatMap(F1<A, Option<B>> f) {
      return f.apply(value);
    }

    @Override
    public Option<A> orElse(Option<A> a) {
      return this;
    }

    @Override
    public <E> Either<E, A> toRight(F0<E> left) {
      return Either.right(value);
    }

    @Override
    public <E> Either<A, E> toLeft(F0<E> right) {
      return Either.left(value);
    }

    @Override
    public A getOrElse(F0<A> a) {
      return value;
    }

    @Override
    public A getOrThrow(RuntimeException e) {
      return value;
    }

    @Override
    public A getOrThrow(String errorMessage) {
      return value;
    }

    @Override
    public Iterator<A> iterator() {
      return new Iterator<A>() {
        private boolean hasNext = true;
        @Override
        public boolean hasNext() {
          return hasNext;
        }

        @Override
        public A next() {
          if(hasNext){
            hasNext = false;
            return value;
          }else {
            throw new NoSuchElementException();
          }
        }
      };
    }
  }

  @SuppressWarnings("unchecked")
  public static <A> Option<A> none(){
    return (Option<A>)NONE;
  }

  public static <A> Option<A> some(final A a){
    return new Some<>(a);
  }

  private static final Option<Object> NONE = new None<>();

  private static final class None<A> extends Option<A>{
    @Override
    @SuppressWarnings("unchecked")
    public <B> Option<B> map(F1<A, B> f) {
      return (Option<B>)this;
    }

    @Override
    public <B> B fold(F1<A, B> f, F0<B> z) {
      return z.apply();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> Option<B> flatMap(F1<A, Option<B>> f) {
      return (Option<B>)this;
    }

    @Override
    public Option<A> orElse(Option<A> a) {
      return a;
    }

    @Override
    public <E> Either<E, A> toRight(F0<E> left) {
      return Either.left(left.apply());
    }

    @Override
    public <E> Either<A, E> toLeft(F0<E> right) {
      return Either.right(right.apply());
    }

    @Override
    public A getOrElse(F0<A> a) {
      return a.apply();
    }

    @Override
    public A getOrThrow(RuntimeException e) {
      throw e;
    }

    @Override
    public A getOrThrow(String errorMessage) {
      throw new RuntimeException(errorMessage);
    }

    private static Iterator<Object> EmptyIterator =
      new Iterator<Object>(){
        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public Object next() {
          throw new NoSuchElementException();
        }
      };

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<A> iterator() {
      return (Iterator<A>)EmptyIterator;
    }
  }
}
