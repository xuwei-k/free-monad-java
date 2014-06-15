package free;

import static free.CharToy.*;

public final class Main {
  static Free<F0.z, Long> fib(final Long n){
    if(n < 2){
      return Free.done(2L);
    }else{
      return
      Trampoline.suspend(() -> fib(n - 1)).flatMap(x ->
        Trampoline.suspend(() -> fib(n - 2)).map(y ->
          x + y
        )
      );
    }
  }

  public static void main(final String[] args){
    final Free<CharToy.z, Unit> program =
      output('A').flatMap(unit1 ->
        bell().flatMap(unit2 ->
          done()
        )
      );

    System.out.println(showProgram(program));

    System.out.println(Trampoline.run(fib(35L)));

    ((Either<Http.HttpError, String>)Free.runFC(Http.sample1, Http.apacheInterpreter, Either.monad())).fold(
      error -> {
        System.out.println(error.code);
        System.out.println(error.url);
        return Unit.unit;
      },
      result -> {
        System.out.println(result);
        return Unit.unit;
      }
    );
  }

  static <R> String showProgram(Free<CharToy.z, R> program){
    return
    program.resume(CharToy.functor).fold(
      r ->
        ((CharToy<Free<CharToy.z, R>>)r).fold(
          (a, next) -> "output " + a + "\n" + showProgram(next),
          (next -> "bell " + "\n" + showProgram(next)),
          "done\n"
        )
      ,
      r ->
        "return " + r + "\n"
    );
  }
}
