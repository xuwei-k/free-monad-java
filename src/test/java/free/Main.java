package free;

import static free.CharToy.*;

public final class Main {

  public static void main(final String[] args){
    final Free<CharToy.z, Unit> program =
      output('A').flatMap(
        new F1<Unit, Free<CharToy.z, Unit>>() {
          @Override
          public Free<CharToy.z, Unit> apply(final Unit unit) {
            return bell().flatMap(new F1<Unit, Free<CharToy.z, Unit>>() {
              @Override
              public Free<CharToy.z, Unit> apply(final Unit unit) {
                return done();
              }
            });
          }
        }
      );

    System.out.println(showProgram(program));
  }

  static <R> String showProgram(Free<CharToy.z, R> program){
    return
    program.resume(CharToy.functor).fold(
      new F1<_1<CharToy.z, Free<CharToy.z, R>>, String>() {
        @Override
        public String apply(final _1<CharToy.z, Free<CharToy.z, R>> r) {
          return
          ((CharToy<Free<CharToy.z, R>>)r).fold(
            new F2<Character, Free<CharToy.z, R>, String>() {
              @Override
              public String apply(final Character a, final Free<CharToy.z, R> next) {
                return
                "output " + a + "\n" + showProgram(next);
              }
            },
            new F1<Free<CharToy.z, R>, String>() {
              @Override
              public String apply(final Free<CharToy.z, R> next) {
                return
                "bell " + "\n" + showProgram(next);
              }
            },
            "done\n"
          );
        }
      },
      new F1<R, String>(){
        @Override
        public String apply(final R r) {
          return "return " + r + "\n";
        }
      }
    );
  }
}