package free;

import java.util.concurrent.atomic.AtomicReference;

public final class Lazy<A> implements _1<Lazy.z, A>{
  public Lazy(final F0<A> f) {
    this.f = f;
  }

  public static final class z{}

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

  public <B> Lazy<B> map(final F1<A, B> f){
    return new Lazy<>(() -> f.apply(get()));
  }

  public <B> Lazy<B> flatMap(final F1<A, Lazy<B>> f){
    return f.apply(get());
  }

}
