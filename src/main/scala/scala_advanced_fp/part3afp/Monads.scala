package com.eugeniusz.scala
package scala_advanced_fp.part3afp

object Monads extends App {

  // our own Try monad
  trait Attempt[+A] {
     def flatMap[B](f: A => Attempt[B]): Attempt[B]

  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try{
        Success(a)  // must be evaluated by name
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)

  /*
  EXERCISE:
  1) implement a Lazy[T] monad = computation which will only be executed when it's needed
  unit/apply in the companion object or the lazy trait
  flatMap that transforms the element into another Lazy instance

  2) Monads = unit + flatMap
     Monads = unit + map + flatten
     Monad[T] {
       def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
       def map[B](f: T => B): Monad[B] = ???
       def flatten(m: Monad[Monad[T]]): Monad[T] = ???
       (have List in mind)
     }
   */

  // 1 - Lazy monad
  // covariant means that Lazy[Cat] is a subtype of Lazy[Animal]
  class Lazy[+A](value: => A) {  // call by name
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
//    def flatMap[B] (f: A => Lazy[B]): Lazy[B] = f(value)  // eager evaluation
    def flatMap[B] (f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)  // lazy evaluation with call by name for parameter
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)  // unit
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

//  println(lazyInstance.use)  // the side effect is executed here, and evaluated only once and now it's cached

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {  // the side effect is executed here, because flatMap is called, and apply is called
    10 * x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  flatMappedInstance.use
  flatMappedInstance2.use

  /*
  left identity:
  unit.flatMap(f) == f(x)
  Lazy(v).flatMap(f) == f(v)

  right identity:
  lazyInstance.flatMap(unit) == lazyInstance
  Lazy(v).flatMap(x => Lazy(x)) == Lazy(v)

  associativity:
  Lazy(v).flatMap(f).flatMap(g) == f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) == f(v).flatMap(g)
   */

  // 2 - map and flatten in terms of flatMap

  /*
  Monad[T] {  // Monad is a type constructor
    def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
    def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x)))  // Monad[B]
    def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)  // Monad[T]

    List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
    List(List(1,2), List(3,4)).flatten = List(List(1,2), List(3,4)).flatMap(x => x) = List(1,2,3,4)
   */

  // *******************************************************************************************************************

  /*
  left identity:

  unit.flatMap(f) == f(x)
  Attempt(x).flatMap(f) == f(x) // Success case!
  Success(x).flatMap(f) == f(x) // proved

  right identity:

  attempt.flatMap(unit) == attempt
  Success(x).flatMap(x => Attempt(x)) == Attempt(x) // proved
  Fail(e).flatMap(...) == Fail(e) // proved

  associativity:

  attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  Fail(e).flatMap(f).flatMap(g) == Fail(e) // proved
  Fail(e).flatMap(x => f(x).flatMap(g)) == Fail(e) // proved

  Success(v).flatMap(f).flatMap(g) ==
  f(v).flatMap(g) OR Fail(e)

  Success(v).flatMap(x => f(x).flatMap(g)) ==
  f(v).flatMap(g) OR Fail(e)
   */



  /*
  Monads are a kind of types which have two operations:
  - unit, also called pure or apply
  - flatMap, also called bind or >>=
  monad transforms a value of type A into a value of type M[A]

  List, Option, Try, Future, Either, Reader, Writer, State, IO, ...are monads
  Monad rules:
  - left identity: unit(x).flatMap(f) == f(x)

  - right identity: m.flatMap(unit) == m

  - associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))

  Monad laws are similar to monoid laws:
  - left identity: unit(x).flatMap(f) == f(x)
  - right identity: m.flatMap(unit) == m
  - associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))

   */

  /**
   * Monad is a type constructor with two operations:
   * - unit
   * - flatMap
   *
   * Monad laws:
   * - left identity: unit(x).flatMap(f) == f(x)
   * - right identity: m.flatMap(unit) == m
   * - associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
   */

}
