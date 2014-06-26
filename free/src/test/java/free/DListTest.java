package free;

public final class DListTest {
  public static boolean test(){
    final java.util.List<Integer> actual = List.range(0, 100000).foldLeft(
      DList.<Integer>empty(),
      (xs, x) -> xs.append1(x)
    ).toList().toJavaList();

    final java.util.List<Integer> expect = List.range(0, 100000).toJavaList();
    return actual.equals(expect);
  }
}