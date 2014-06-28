package free

import scala.language.reflectiveCalls
import scala.language.higherKinds

object Java2Scalaz {

  private[free] def byName2F0[A](a: => A): free.F0[A] =
    new F0[A] {
      def apply = a
    }

  private[free] def toJavaF1[A, B](f: A => B): free.F1[A, B] =
    new F1[A, B] {
      def apply(a: A) = f(a)
    }

  def functor[F[A] <: {
    def map[B](f : free.F1[A, B]): F[B]
  }]: scalaz.Functor[F] = new JavaFunctor[F]{}

  def bind[F[A] <: {
    def flatMap[B](f: free.F1[A, F[B]]): F[B]
    def map[B](f: free.F1[A, B]): F[B]
  }]: scalaz.Bind[F] = new JavaBind[F]{}

  implicit val list: scalaz.MonadPlus[free.List] =
    new JavaMonadPlus[List] {
      override def point[A](a: => A) =
        free.List.single(a)
      override def empty[A] =
        free.List.nil()
    }

  implicit val option: scalaz.MonadPlus[free.Option] =
    new JavaMonadPlus[Option] {
      override def point[A](a: => A) =
        free.Option.some(a)
      override def empty[A] =
        free.Option.none()
    }

  implicit def either[L]: scalaz.Monad[({type l[a] = free.Either[L, a]})#l] =
    new JavaMonad[({type l[a] = Either[L, a]})#l] {
      override def point[A](a: => A) =
        free.Either.right(a)
    }

  implicit def f1[A]: scalaz.Monad[({type l[a] = free.F1[A, a]})#l] =
    new JavaMonad[({type l[a] = F1[A, a]})#l] {
      override def point[B](a: => B) =
        free.F1.constant(a)
    }
}

import Java2Scalaz._

private trait JavaFunctor[F[A] <: {
  def map[B](f: free.F1[A, B]): F[B]
}] extends scalaz.Functor[F] {
  override final def map[A, B](fa: F[A])(f: A => B): F[B] =
    fa.map(toJavaF1(f))
}

private trait JavaBind[F[A] <: {
  def flatMap[B](f: free.F1[A, F[B]]): F[B]
  def map[B](f: free.F1[A, B]): F[B]
}] extends scalaz.Bind[F] with JavaFunctor[F]{
  override final def bind[A, B](fa: F[A])(f: A => F[B]) =
    fa.flatMap(toJavaF1(f))
}

private trait JavaMonad[F[A] <: {
  def flatMap[B](f: free.F1[A, F[B]]): F[B]
  def map[B](f: free.F1[A, B]): F[B]
}] extends scalaz.Monad[F] with JavaBind[F]

private trait JavaMonadPlus[F[A] <: {
  def flatMap[B](f: free.F1[A, F[B]]): F[B]
  def map[B](f: free.F1[A, B]): F[B]
  def plus(f: F0[F[A]]): F[A]
}] extends scalaz.MonadPlus[F] with JavaBind[F]{
  override final def plus[B](a: F[B], b: => F[B]) =
    a.plus(byName2F0(b))
}

