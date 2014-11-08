package free;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Lazy<A> implements _1<Lazy.z, A>{
  private Lazy(){}

  public static final class z{}

  public abstract A get();

  public <B> Lazy<B> map(final F1<A, B> f){
    return new Impl<>(() -> f.apply(get()));
  }

  public <B> Lazy<B> flatMap(final F1<A, Lazy<B>> f){
    return f.apply(get());
  }

  public static <B> Lazy<B> of(final F0<B> b){
    return new Impl<>(b);
  }

  public static <B> Lazy<B> value(final B b){
    return new Value<>(b);
  }

  private final static class Value<A> extends Lazy<A>{
    private final A a;

    private Value(final A a) {
      this.a = a;
    }

    @Override
    public A get() {
      return a;
    }
  }

  private final static class Impl<A> extends Lazy<A>{
    Impl(final F0<A> f) {
      this.f = f;
    }
    private final AtomicReference<A> cached = new AtomicReference<A>(null);
    volatile private F0<A> f;

    public A get() {
      A value = this.cached.get();
      if (value == null) {
        synchronized (this.cached) {
          value = this.cached.get();
          if (value == null) {
            value = f.apply();
            this.cached.set(value);
            f = null;
          }
        }
      }
      return value;
    }
  }
}
