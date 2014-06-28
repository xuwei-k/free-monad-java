package free

import org.scalacheck.Arbitrary

import scalaz.Equal
import scalaz.std.anyVal._
import scalaz.scalacheck.ScalaCheckBinding.ArbitraryMonad
import scalaz.scalacheck.ScalazProperties.monadPlus
import scalaz.std.option.optionEqual
import free.Java2Scalaz.option

object OptionTest extends scalaz.SpecLite {

  private def scala2java[A](a: scala.Option[A]): free.Option[A] =
    a.fold(free.Option.none[A])(free.Option.some)

  private def java2scala[A](a: free.Option[A]): scala.Option[A] =
    a.fold(a => scala.Option(a), () => scala.Option.empty[A])

  implicit def optionAab[A: Arbitrary]: Arbitrary[free.Option[A]] =
    scalaz.Functor[Arbitrary].map(implicitly[Arbitrary[scala.Option[A]]])(
      scala2java
    )

  implicit def optionEq[A](implicit A: Equal[A]): Equal[free.Option[A]] =
    scalaz.Equal[scala.Option[A]].contramap(java2scala)

  checkAll(monadPlus.strongLaws[free.Option])

}
