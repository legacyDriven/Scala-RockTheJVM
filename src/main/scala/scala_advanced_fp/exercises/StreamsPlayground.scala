package com.eugeniusz.scala
package scala_advanced_fp.exercises

import scala_advanced_fp.part2afp.LazyEvaluation.MyStream

import scala.annotation.tailrec

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

  def takeAsList(n: Int): List[A] = take(n).toList()  // takes the first n elements out of this stream and converts them to a list

 /*
  [1, 2, 3].toList([]) =  // tail.toList([1]) is evaluated first
  [2, 3].toList([1]) =  // tail.toList([2, 1]) is evaluated first
  [3].toList([2, 1]) =  // tail.toList([3, 2, 1]) is evaluated first
  [].toList([3, 2, 1]) =  // tail.toList([3, 2, 1]) is evaluated first
  [3, 2, 1]   // the result is reversed
  */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] = { // preserves lazy evaluation
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
  }
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException

  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)

  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing => Unit): Unit = ()

  def map[B](f: Nothing => B): MyStream[B] = this

  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this  // takes the first n elements out of this stream

}

class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false

  override val head: A = hd

  override lazy val tail: MyStream[A] = tl // call by need

  /*
    val s = new Cons(1, EmptyStream)
    val prepended = 1 #:: s = new Cons(1, s)
   */
  def #::[B >: A](element: B): MyStream[B] =
    new Cons(element, this)

  // if the second parameter is a call by name parameter (anotherStream: => MyStream[B],
  // it is evaluated only when it is needed, otherwise it is evaluated immediately
  // and the result is passed to the method, what may cause stack overflow error in case of infinite streams
  // (anotherStream: MyStream[B])
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] =
    new Cons(head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {  // apply f to every element of this stream
    f(head)  // apply f to the head
    tail.foreach(f)  // apply f to the tail
  }

  /*
    s = new Cons(1, ?)
    mapped = s.map(_ + 1) = new Cons(2, s.tail.map(_ + 1)) - mapped.tail is not evaluated until it is needed
      ... mapped.tail
   */
  def map[B](f: A => B): MyStream[B] =
    new Cons(f(head), tail.map(f))  // recursive call, preserves lazy evaluation

  def flatMap[B](f: A => MyStream[B]): MyStream[B] = // preserves lazy evaluation
    f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] = {
    if (predicate(head)) new Cons(head, tail.filter(predicate))  // preserves lazy evaluation
    else tail.filter(predicate)
  }

  def take(n: Int): MyStream[A] = {
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n - 1))  // preserves lazy evaluation
  }
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = {
    new Cons(start, MyStream.from(generator(start))(generator))  // from(1)(x => x + 1) = new Cons(1, from(2)(x => x + 1))
  }
}

object StreamsPlayground extends App {

  val naturals = MyStream.from(1)(_ + 1)  // stream of natural numbers
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)  // lazy evaluation

  val startFrom0 = 0 #:: naturals  // prepend operator, gives a stream starting from 0 to infinity
  println(startFrom0.head)

  startFrom0.take(10000).foreach(println)  // takes the first 10000 elements out of this stream

  // map, flatMap

  println(startFrom0.map(_ * 2).take(100).toList())  // takes the first 100 elements out of this stream and converts them to a list
  // List(0, 2, 4, 6, 8, 10, ...)

  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
  // takes the first 10 elements out of this stream and converts them to a list
  // List(0, 1, 1, 2, 2, 3, 3, 4, 4, 5)

  // filter
  println(startFrom0.filter(_ < 10).take(10).take(20).toList())  // takes the first 10 elements out of this stream and converts them to a list
  // List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
  // now stream is finite, without take(10) it would be infinite and the program would crash with stack overflow error
  // taking more than 10 elements is not possible, because the stream is finite, and would crush,
  // if we tried to take more than 10 elements, but if we added take(20) after take(10), it would work, because
  // take(20) would be applied to the stream after take(10) and would take the first 20 elements out of the stream

  // Exercises on streams
  // 1 - stream of Fibonacci numbers

  /*
    [first, [ ...
    [first, fibo(second, first + second)
   */

  // 1 - stream of Fibonacci numbers
  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] =
    new Cons(first, fibonacci(second, first + second))  // preserves lazy evaluation

  println(fibonacci(1, 1).take(100).toList())
  println(fibonacci(1, 1).take(100).toList().last)  // last element of the stream of Fibonacci numbers

  // 2 - stream of prime numbers with Eratosthenes' sieve
  /*
    [ 2 3 4 5 6 7 8 9 10 11 12 13 ...
    [ 2 3 5 7 9 11 13 ...
    [ 2 eratosthenes applied to (numbers filtered by n % 2 != 0)
    [ 2 3 eratosthenes applied to (numbers filtered by n % 3 != 0)
    [ 2 3 5 eratosthenes applied to (numbers filtered by n % 5 != 0)
    filter out all numbers divisible by 2
    [2 3 5 7 9 11 ... ]
    filter out all numbers divisible by 3
    [2 3 5 7 11 13 17 ... ]
    filter out all numbers divisible by 5
      ...
   */
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))  // preserves lazy evaluation

  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())

}


