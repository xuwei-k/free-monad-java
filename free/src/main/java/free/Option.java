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

  public abstract A getOrElse(final F0<A> a);

  public abstract A getOrThrow(final RuntimeException e);

  public abstract A getOrThrow(final String errorMessage);

  public static MonadPlus<Option.z> monadPlus(){
    return new MonadPlus<z>() {
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
    };
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
