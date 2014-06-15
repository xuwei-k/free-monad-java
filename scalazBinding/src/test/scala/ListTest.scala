package free.test

import free.{List => FList}
import org.scalacheck._
import scalaz.{Show, Equal}
import scalaz.std.list._
import scalaz.std.anyVal._
import scalaz.syntax.contravariant._
import scala.collection.JavaConversions._

object ListTest extends scalaz.SpecLite {

  implicit def FListArb[A: Arbitrary]: Arbitrary[FList[A]] =
    Arbitrary(implicitly[Arbitrary[List[A]]].arbitrary.map(l => FList.of(l: _*)))

  implicit def FListEqual[A](implicit A: Equal[A]): Equal[FList[A]] =
    Equal[List[A]].contramap(_.toJavaList.toList)

  implicit def FListShow[A](implicit A: Show[A]): Show[FList[A]] =
    Show[List[A]].contramap(_.toJavaList.toList)

  property("reverse") = Prop.forAll { list: FList[Int] =>
    list.reverse().toJavaList().toList must_=== list.toJavaList().reverse.toList
    list.reverse().reverse() must_=== list
  }

  property("map") = Prop.forAll { list: FList[Int] =>
    list.map(_ + 1).toJavaList.toList must_=== list.toJavaList.toList.map(_ + 1)
  }

  property("append") = Prop.forAll { (a1: FList[Int], a2: FList[Int]) =>
    a1.append(a2).toJavaList.toList must_=== (
      a1.toJavaList.toList ::: a2.toJavaList.toList
    )
  }

  property("length") = Prop.forAll { list: FList[Int] =>
    list.length must_=== list.toJavaList.size
  }
}

