package com.eugeniusz.scala
package scala_essentials.part4pm

import scala.util.Random

object PatternMatching extends App {

  // switch on steroids
  val random = new Random
  val x = random.nextInt(10)

  val description = x match {
    case 1 => "the ONE"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else" // _ = WILDCARD
  }

  println(x)
  println(description)

  // 1. Decompose values
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I can't drink in the US"  // guards
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"  // guards
    case _ => "I don't know who I am"  // guards, wildcard
  }

  println(greeting)

  /*
    1. cases are matched in order
    2. what if no cases match? MatchError
    3. type of the PM expression? unified type of all the types in all the cases
    4. PM works really well with case classes
   */

  // PM on sealed hierarchies
  sealed class Animal  // sealed - all the subtypes must be declared in the same file
  case class Dog(breed: String) extends Animal  // case - compiler adds some syntactic sugar
  case class Parrot(greeting: String) extends Animal  // case - compiler adds some syntactic sugar

  val animal: Animal = Dog("Terra Nova")  // upcasting
  animal match {  // PM works really well with case classes
    case Dog(someBreed) => println(s"Matched a dog of the $someBreed breed")  // PM works really well with case classes
  }

  // match everything
  val isEven = x match {
    case n if n % 2 == 0 => true
    case _ => false
  }  // WHY?!

  val isEvenCond = if (x % 2 == 0) true else false  // WHY?!
  val isEvenNormal = x % 2 == 0  // WHY?!

  /*
    Exercise
    simple function uses PM
      takes an Expr => human readable form

      Sum(Number(2), Number(3)) => 2 + 3
      Sum(Number(2), Number(3), Number(4)) => 2 + 3 + 4
      Prod(Sum(Number(2), Number(1)), Number(3)) => (2 + 1) * 3
      Sum(Prod(Number(2), Number(1)), Number(3)) => 2 * 1 + 3
   */

  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr

  def show(e: Expr): String = e match {
    case Number(n) => s"$n" // return s"$n"  // return is not necessary
    case Sum(e1, e2) => show(e1) + " + " + show(e2)
    case Prod(e1, e2) => {
      def maybeShowParentheses(expr: Expr) = expr match {
        case Prod(_, _) => show(expr)
        case Number(_) => show(expr)
        case _ => "(" + show(expr) + ")"  // _ = WILDCARD
      }
      maybeShowParentheses(e1) + " * " + maybeShowParentheses(e2)
    }
  }

  println(show(Sum(Number(2), Number(3))))  // 2 + 3
  println(show(Sum(Sum(Number(2), Number(3)), Number(4))))  // 2 + 3 + 4
  println(show(Prod(Sum(Number(2), Number(1)), Number(3))))  // (2 + 1) * 3
  println(show(Sum(Prod(Number(2), Number(1)), Number(3))))  // 2 * 1 + 3

}
