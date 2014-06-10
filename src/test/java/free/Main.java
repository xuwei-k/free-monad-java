package free;

import static free.CharToy.*;
import lombok.val;

public final class Main {

  public static void main(final String[] args){
    val program =
      output('A').flatMap(unit1 ->
        bell().flatMap(unit2 ->
          done()
        )
      );

    System.out.println(showProgram(program));
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
