package free;

public interface MonadPlus<F> extends Monad<F>, PlusEmpty<F>{
  public static interface WithDefault<F> extends MonadPlus<F>, Monad.WithDefault<F>, PlusEmpty<F>{
  }
}
