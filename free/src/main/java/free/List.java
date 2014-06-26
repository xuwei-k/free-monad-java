package free;

import java.util.ArrayList;

public abstract class List<A> implements _1<List.z, A> {
  public static final class z{}

  private List(){}

  public abstract Option<A> headOption();

  public abstract Option<List<A>> tailOption();

  public final <B> List<B> map(final F1<A, B> f){
    return reverseMap(f).reverse();
  }

  public abstract <B> B cata(final F0<B> empty, final F2<A, List<A>, B> nonEmpty);

  public abstract <B> List<B> reverseMap(F1<A, B> f);

  public abstract List<A> reverse();

  public abstract <B> List<B> flatMap(F1<A, List<B>> f);

  public final <B> B foldLeft(B z, F2<B, A, B> f){
    B result = z;
    List<A> src = this;
    while(src.nonEmpty()){
      result = f.apply(result, ((Cons<A>)src).head);
      src = ((Cons<A>)src).tail;
    }
    return result;
  }

  public final <B> B foldMap(final F1<A, B> f, final Monoid<B> F){
    B result = F.zero();
    List<A> list = this;
    while(list.nonEmpty()){
      result = f.apply(((Cons<A>)list).head);
      list = ((Cons<A>)list).tail;
    }
    return result;
  }

  public final <B> B foldRight(B z, F2<A, B, B> f){
    return reverse().foldLeft(z, f.swap());
  }

  public final <F, B> _1<F, List<B>> traverse(final F1<A, _1<F, B>> f, final Applicative<F> F){
    return foldRight(
      F.point(() -> nil()),
      (a, fbs) -> F.apply2(() -> f.apply(a), () -> fbs, Cons::new)
    );
  }

  public final boolean nonEmpty(){
    return this instanceof Cons;
  }

  public final boolean isEmpty(){
    return this instanceof Nil;
  }

  public final int length(){
    int len = 0;
    List<A> list = this;
    while(list.nonEmpty()){
      len++;
      list = ((Cons<A>)list).tail;
    }
    return len;
  }

  public final List<A> append(final List<A> list){
    return reverse().foldLeft(list, (as, a) -> new Cons<>(a, as));
  }

  public final List<A> cons(final A head){
    return new Cons<>(head, this);
  }

  public final java.util.List<A> toJavaList(){
    java.util.List<A> result = new ArrayList<>();
    List<A> self = this;
    while(self.nonEmpty()){
      result.add(((Cons<A>)self).head);
      self = ((Cons<A>)self).tail;
    }
    return result;
  }

  private static Nil<Object> nil = new Nil<>();

  @SuppressWarnings("unchecked")
  public static <A> List<A> nil(){
    return (List)nil;
  }

  @SafeVarargs
  public static <A> List<A> of(final A... as){
    List<A> list = nil();
    for(int i = as.length - 1; i >= 0; i--){
      list = new Cons<>(as[i], list);
    }
    return list;
  }

  public static List<Integer> range(final int start, final int end){
    assert start <= end;
    List<Integer> list = nil();
    for(int i = end; start <= i; i--){
      list = new Cons<>(i, list);
    }
    return list;
  }

  public static <A> List<A> join(final List<List<A>> list){
    return list.reverse().foldLeft(nil(), (t, h) -> h.append(t));
  }

  public static <A> List<A> single(final A a){
    return new Cons<>(a, nil());
  }

  private static Instance instance = new Instance();

  public static MonadPlus<z> monadPlus = instance;

  public static Traverse<z> traverse = instance;

  private static final class Instance implements MonadPlus.WithDefault<z>, Traverse<z> {
    @Override
    public <A> _1<z, A> point(F0<A> a) {
      return single(a.apply());
    }

    @Override
    public <A, B> _1<z, B> flatMap(F1<A, _1<z, B>> f, _1<z, A> fa) {
      return ((List<A>)fa).flatMap(a -> (List<B>)f.apply(a));
    }

    @Override
    public <A> _1<z, A> empty() {
      return nil();
    }

    @Override
    public <A> _1<z, A> plus(_1<z, A> a1, _1<z, A> a2) {
      return ((List<A>)a1).append((List<A>)a2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <G, A, B> _1<G, _1<z, B>> traverse(_1<z, A> fa, F1<A, _1<G, B>> f, Applicative<G> G) {
      return (_1<G, _1<z, B>>)(Object)((List<A>)fa).traverse(f, G);
    }

    @Override
    public <A, B> B foldMap(F1<A, B> f, _1<z, A> fa, Monoid<B> B) {
      return ((List<A>)fa).foldMap(f, B);
    }

    @Override
    public <A, B> B foldLeft(_1<z, A> fa, B z, F2<B, A, B> f) {
      return ((List<A>)fa).foldLeft(z, f);
    }

    @Override
    public <A, B> _1<z, B> map(F1<A, B> f, _1<z, A> fa) {
      return ((List<A>)fa).map(f);
    }
  }

  private static final class Cons<A> extends List<A> {
    private final A head;
    private final List<A> tail;

    private Cons(A head, List<A> tail) {
      this.head = head;
      this.tail = tail;
    }

    @Override
    public Option<A> headOption() {
      return Option.some(head);
    }

    @Override
    public Option<List<A>> tailOption() {
      return Option.some(tail);
    }

    @Override
    public <B> B cata(F0<B> empty, F2<A, List<A>, B> nonEmpty) {
      return nonEmpty.apply(head, tail);
    }

    @Override
    public <B> List<B> reverseMap(F1<A, B> f) {
      List<B> bs = nil();
      List<A> as = this;
      while(as.nonEmpty()){
        bs = new Cons<>(f.apply(((Cons<A>)as).head), bs);
        as = ((Cons<A>) as).tail;
      }
      return bs;
    }

    @Override
    public List<A> reverse() {
      List<A> reversed = nil();
      List<A> self = this;
      while(self.nonEmpty()){
        reversed = new Cons<>(((Cons<A>)self).head, reversed);
        self = ((Cons<A>) self).tail;
      }
      return reversed;
    }

    @Override
    public <B> List<B> flatMap(F1<A, List<B>> f) {
      return join(map(f));
    }
  }

  private final static class Nil<A> extends List<A> {
    @Override
    public Option<A> headOption() {
      return Option.none();
    }

    @Override
    public Option<List<A>> tailOption() {
      return Option.none();
    }

    @Override
    public <B> B cata(F0<B> empty, F2<A, List<A>, B> nonEmpty) {
      return empty.apply();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> List<B> reverseMap(F1<A, B> f) {
      return (List<B>)this;
    }

    @Override
    public List<A> reverse() {
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> List<B> flatMap(F1<A, List<B>> f) {
      return (List<B>)this;
    }
  }
}