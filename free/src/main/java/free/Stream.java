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

  public final <B> Stream<B> flatMap(final F1<A, Stream<B>> f){
    return foldRight(() -> empty(), (h, t) -> f.apply(h).append(t));
  }

  public final Stream<A> append(final F0<Stream<A>> stream){
    return foldRight(stream, (h, t) -> new Cons<>(h, Lazy.of(t)));
  }

  public final <B> B foldRight(final F0<B> z, final F2<A, F0<B>, B> f){
    if(isEmpty()){
      return z.apply();
    }else{
      return f.apply(unsafeHead(), () -> unsafeTail().foldRight(z, f));
    }
  }

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

  public static <B> Stream<B> fromList(final List<B> list){
    return list.foldRight(Stream.<B>empty(), (h, t) -> new Cons<>(h, Lazy.value(t)));
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

  public static <B> Stream<B> narrow(final _1<z, B> a){
    return (Stream<B>)a;
  }

  public static <B> Stream<B> point(final B b){
    return new Cons<>(b, Lazy.value(empty()));
  }

  public static final MonadPlus<z> monadPlus = Instance.instance;

  private static final class Instance implements MonadPlus.WithDefault<z>, Foldable<z> {
    private static final Instance instance = new Instance();

    private Instance(){}

    @Override
    public <A> _1<z, A> point(F0<A> a) {
      return Stream.point(a.apply());
    }

    @Override
    public <A, B> _1<z, B> flatMap(F1<A, _1<z, B>> f, _1<z, A> fa) {
      final F1<A, Stream<B>> f0 = f.map(Stream::narrow);
      return narrow(fa).flatMap(f0);
    }

    @Override
    public <A> _1<z, A> empty() {
      return Stream.empty();
    }

    @Override
    public <A> _1<z, A> plus(_1<z, A> a1, _1<z, A> a2) {
      return narrow(a1).append(() -> narrow(a2));
    }

    @Override
    public <A, B> B foldMap(F1<A, B> f, _1<z, A> fa, Monoid<B> B) {
      return narrow(fa).foldLeft(B.zero(), (b, a) -> B.append(b, f.apply(a)));
    }

    @Override
    public <A, B> B foldLeft(_1<z, A> fa, B z, F2<B, A, B> f) {
      return narrow(fa).foldLeft(z, f);
    }
  }
}
