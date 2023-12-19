package com.eugeniusz.scala
package scala_advanced_fp.part2afp

import scala_advanced_fp.part2afp.LazyEvaluation.MyStream

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications:
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition

  println(if (simpleCondition && lazyCondition) "yes" else "no")  // no, lazyCondition is not evaluated, because simpleCondition is false

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED, not call by name, because it is evaluated only once
    lazy val t = n // only evaluated once
    t + t + t + 1
  }

  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue)) // waiting 127
  // use lazy vals

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)

  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20) // List(25, 23)

  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20) // lazy vals under the hood

  println
  println(gt20Lazy) // scala.collection.TraversableLike$WithFilter@6e8cf4c6, no evaluation yet

  gt20Lazy.foreach(println) // 25, 23, evaluation happens here
  /*scala.collection.IterableOps$WithFilter@5ae9a829
  1 is less than 30?
  1 is greater than 20?
  25 is less than 30?
  25 is greater than 20?
  25
  40 is less than 30?
  5 is less than 30?
  5 is greater than 20?
  23 is less than 30?
  23 is greater than 20?
  23
  */

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals!
  } yield a + 1
  // List[Int] = List(3)
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int] = List(3)

  /*
    Exercise: implement a lazily evaluated, singly linked STREAM of elements.

    naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
    naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
    naturals.foreach(println) // will crash - infinite!
    naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
   */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]
    def #::[B >: A](element: B): MyStream[B] // prepend operator, B >: A means that B is a supertype of A
    def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concatenate two streams
    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]
    def take(n: Int): MyStream[A] // takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]

 /*
     Exercise 1: implement a lazily evaluated, singly linked STREAM of elements.

      naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
      naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
      naturals.foreach(println) // will crash - infinite!
      naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
    */

    }
}







