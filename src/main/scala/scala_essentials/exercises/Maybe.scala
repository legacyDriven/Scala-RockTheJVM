package com.eugeniusz.scala
package scala_essentials.exercises

abstract class Maybe[+T] {

  def map[B](function: T => B): Maybe[B]
  def flatMap[B] (function: T => Maybe[B]): Maybe[B]
  def filter(predicate: T => Boolean): Maybe[T]

}

case object MaybeNot extends Maybe[Nothing] {

  def map[B](function: Nothing => B): Maybe[B] = MaybeNot
  def flatMap[B] (function: Nothing => Maybe[B]): Maybe[B] = MaybeNot
  def filter(predicate: Nothing => Boolean): Maybe[Nothing] = MaybeNot

}

case class Just[+T](value: T) extends Maybe[T] {

  def map[B](function: T => B): Maybe[B] = Just(function(value))
  def flatMap[B](function: T => Maybe[B]): Maybe[B] = function(value)
  def filter(predicate: T => Boolean): Maybe[T] =
    if(predicate(value)) this
    else MaybeNot

}

object MaybeTest extends App {

  val just3 = Just(3)  // Just(3)
  println(just3)  // Just(3)
  println(just3.map(_ * 2))  // Just(6)
  println(just3.flatMap(x => Just(x % 2 == 0 )))  // Just(false)
  println(just3.filter(_ % 2 == 0))  // MaybeNot
}
