package free;

public interface Monoid<F> extends Semigroup<F>{
  public F zero();
}
