package free;

public abstract class Stream<A> implements _1<Stream.z, A> {
  public static final class z{}

  private Stream(){}

  public abstract Option<A> headOption();

  public abstract Option<Stream<A>> tailOption();

  private A unsafeHead(){
    return ((Cons<A>)this).head;
  }

  private Stream<A> unsafeTail(){
    return ((Cons<A>)this).tail.get();
  }

  public abstract <B> B cata(F0<B> empty, F2<A, Stream<A>, B> cons);

  public abstract <B> Stream<B> map(final F1<A, B> f);

  public abstract boolean isEmpty();

  public final boolean nonEmpty(){
    return !isEmpty();
  }

  public final List<A> toList(){
    List<A> list = List.nil();
    Stream<A> src = this;
    while (src.nonEmpty()){
      list = list.cons(src.unsafeHead());
      src = src.unsafeTail();
    }
    return list.reverse();
  }

  public final int length(){
    int len = 0;
    Stream<A> src = this;
    while(src.nonEmpty()){
      len++;
      src = src.unsafeTail();
    }
    return len;
  }

  public final <B> B foldLeft(final B z, final F2<B, A, B> f){
    B b = z;
    Stream<A> src = this;
    while(src.nonEmpty()){
      b = f.apply(b, src.unsafeHead());
      src = src.unsafeTail();
    }
    return b;
  }

  private final static class Cons<A> extends Stream<A>{
    private final A head;
    private final Lazy<Stream<A>> tail;

    private Cons(A head, Lazy<Stream<A>> tail) {
      this.head = head;
      this.tail = tail;
    }

    @Override
    public Option<A> headOption() {
      return Option.some(head);
    }

    @Override
    public Option<Stream<A>> tailOption() {
      return Option.some(tail.get());
    }

    @Override
    public <B> B cata(F0<B> empty, F2<A, Stream<A>, B> cons) {
      return cons.apply(head, tail.get());
    }

    @Override
    public <B> Stream<B> map(F1<A, B> f) {
      return new Cons<>(f.apply(head), tail.map(x -> x.map(f)));
    }

    @Override
    public boolean isEmpty() {
       return false;
    }
  }

  private final static Empty<Object> empty = new Empty<>();

  @SuppressWarnings("unchecked")
  private static <B> Stream<B> empty(){
    return (Stream<B>)empty;
  }

  private final static class Empty<A> extends Stream<A>{

    @Override
    public Option<A> headOption() {
      return Option.none();
    }

    @Override
    public Option<Stream<A>> tailOption() {
      return Option.none();
    }

    @Override
    public <B> B cata(F0<B> empty, F2<A, Stream<A>, B> cons) {
      return empty.apply();
    }

    @Override
    public <B> Stream<B> map(F1<A, B> f) {
      return empty();
    }

    @Override
    public boolean isEmpty() {
      return true;
    }
  }
}
